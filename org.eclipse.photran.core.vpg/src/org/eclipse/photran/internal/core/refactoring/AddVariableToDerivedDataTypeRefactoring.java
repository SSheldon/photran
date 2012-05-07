/*******************************************************************************
 * Copyright (c) 2011 UFSM - Universidade Federal de Santa Maria (www.ufsm.br).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.photran.internal.core.refactoring;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.photran.internal.core.analysis.binding.Definition;
import org.eclipse.photran.internal.core.analysis.binding.ScopingNode;
import org.eclipse.photran.internal.core.parser.ASTDerivedTypeDefNode;
import org.eclipse.photran.internal.core.parser.ASTEntityDeclNode;
import org.eclipse.photran.internal.core.parser.ASTObjectNameNode;
import org.eclipse.photran.internal.core.parser.ASTTypeDeclarationStmtNode;
import org.eclipse.photran.internal.core.parser.IASTListNode;
import org.eclipse.photran.internal.core.parser.IASTNode;
import org.eclipse.photran.internal.core.parser.IBodyConstruct;
import org.eclipse.photran.internal.core.refactoring.infrastructure.FortranEditorRefactoring;
import org.eclipse.photran.internal.core.vpg.PhotranTokenRef;

/**
 * Refactoring that adds a variable to a derived data type.
 * 
 * @author Gustavo Risetti
 */
@SuppressWarnings("nls") // TODO: Externalize strings
public class AddVariableToDerivedDataTypeRefactoring extends FortranEditorRefactoring {

    private static final String OPS = "ops"; //$NON-NLS-1$

    private LinkedList<IBodyConstruct> statements_nodes = new LinkedList<IBodyConstruct>();
    private LinkedList<PhotranTokenRef> references = new LinkedList<PhotranTokenRef>();
    private String derivedTypeName;
    private String derivedTypeVariableName;
    private PhotranTokenRef derived_data_type_ref = null;

    @Override
    public String getName() {
        return "Add Variable To Derived Data Type";
    }

    public void setDerivedTypeVariableName(String name){
        assert name != null;
        derivedTypeVariableName = name;
    }

    public void setDerivedTypeName(String name){
        assert name != null;
        derivedTypeName = name;
    }

	// Search the beginning of variable declarations in each selected row, and checks
	// if are used attributes that are not allowed in derived types.
    private String findStatements(String s){
        final String[] attributes_not_allowed = {"parameter", "intent", "target", "optional", "save", "external", "intrinsic"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
        final String[] types_allowed = {"integer", "real", "complex", "character", "logical"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
        String statement_line = new String();
        boolean has_type_in_line = false;
        boolean init = false;
        int j=0;
        if(s.length() == 0){
            return OPS;
        }else{
            for(int i = 0; i < s.length(); i++){
                if(s.charAt(i) == ' ' || s.charAt(i) == '\t' && init == false){
                   j++;
                }else{
                    init = true;
                    break;
                }
            }
            if(s.charAt(j) == '!'){
                return OPS;
            }else{
                statement_line = s.substring(j);
            }
        }
        for(String type : types_allowed){
            if(statement_line.toLowerCase().contains(type)){
                has_type_in_line = true;
            }
        }
        for(String attribute : attributes_not_allowed){
            if(statement_line.toLowerCase().contains(attribute)){
                has_type_in_line = false;
            }
        }
        if(has_type_in_line){
            return statement_line;
        }else{
            return OPS;
        }
    }

    @Override
    protected void doCheckInitialConditions(RefactoringStatus status, IProgressMonitor pm) throws PreconditionFailure {
        final String SELECT_DECLARATION_WARNING = "Please select a variable declaration (or a list of variable declarations).";
        final String USE_IN_DERIVED_TYPE_WARNING = "You can not use this refactoring within a Derived Data Type that already exists.";
        ensureProjectHasRefactoringEnabled(status);
        // Checks if variables were selected in a derived type that already exists.
        IASTNode derived_type_node = findEnclosingNode(astOfFileInEditor, selectedRegionInEditor);
        ScopingNode derived_type_scope = ScopingNode.getEnclosingScope(derived_type_node);
        if(derived_type_scope instanceof ASTDerivedTypeDefNode){
            fail(USE_IN_DERIVED_TYPE_WARNING);
        }
        String selected_text = selectedRegionInEditor.getText();
        if (selected_text.isEmpty()){
            fail(SELECT_DECLARATION_WARNING);
        }else{
            String selected_lines[] = selected_text.split("\n");
            for(String s : selected_lines){
                s = findStatements(s);
                if(!s.equalsIgnoreCase(OPS)){
                    statements_nodes.add(parseLiteralStatement(s));
                }
            }
            LinkedList<IBodyConstruct> nodes_to_remove = new LinkedList<IBodyConstruct>();
            for( IBodyConstruct node : statements_nodes){
                if(!(node instanceof ASTTypeDeclarationStmtNode)){
                    nodes_to_remove.add(node);
                }
            }
            // List of variables to be included in the derived type.
            statements_nodes.removeAll(nodes_to_remove);
        }
        if(statements_nodes.isEmpty()){
            fail(SELECT_DECLARATION_WARNING);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void doCheckFinalConditions(RefactoringStatus status, IProgressMonitor pm) throws PreconditionFailure {
        final String VALID_NAMES_WARNING = "Fill in the fields with valid values.";
        final String SPACE_TD_WARNING = "The name of the Derived Data Type can not contain spaces and exclamation points.";
        final String SPACE_VTD_WARNING = "The name of the Derived Data Type instance can not contain spaces and exclamation points.";
        final String NUMERIC_DIGITS_WARNING = "The names can not start with numeric digits.";
        final String DERIVED_DATA_TYPE_REF_WARNING = "The Derived Data Type name entered was not found. Make sure you typed correctly.";
        final String DERIVED_DATA_TYPE_INSTANCE_WARNING = "The Derived Data Type instance entered was not found. Make sure you typed correctly.";
        final Character[] numeric_digits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        boolean match_name = false;
        if(derivedTypeName.length() < 1 || derivedTypeVariableName.length() < 1){
            fail(VALID_NAMES_WARNING);
        }
        for(int i = 0; i< derivedTypeName.length(); i++){
            if(derivedTypeName.charAt(i) == ' ' || derivedTypeName.charAt(i) == '!' || derivedTypeName.charAt(i) == '\t'){
                fail(SPACE_TD_WARNING);
            }
        }
        for(int i = 0; i< derivedTypeVariableName.length(); i++){
            if(derivedTypeVariableName.charAt(i) == ' ' || derivedTypeVariableName.charAt(i) == '!' || derivedTypeVariableName.charAt(i) == '\t'){
                fail(SPACE_VTD_WARNING);
            }
        }
        for(int i=0; i<numeric_digits.length; i++){
            if(derivedTypeName.charAt(0) == numeric_digits[i] || derivedTypeVariableName.charAt(0) == numeric_digits[i]){
                fail(NUMERIC_DIGITS_WARNING);
            }
        }
        // Checks if the derived type is spelled correctly.
        IASTNode node = findEnclosingNode(astOfFileInEditor, selectedRegionInEditor);
        ScopingNode scope = ScopingNode.getEnclosingScope(node);
        List<ScopingNode> contained_scopes = scope.getAllContainedScopes();
        for(ScopingNode s : contained_scopes){
            if(s instanceof ASTDerivedTypeDefNode){
                if(((ASTDerivedTypeDefNode)s).getDerivedTypeStmt().getTypeName().getText().equalsIgnoreCase(derivedTypeName)){
                    derived_data_type_ref = ((ASTDerivedTypeDefNode)s).getRepresentativeToken();
                }
            }
        }
        if(derived_data_type_ref == null){
           fail(DERIVED_DATA_TYPE_REF_WARNING);
        }
        // Checks if the instance name of the derived type is spelled correctly.
        IASTListNode<IASTNode> body = (IASTListNode<IASTNode>)scope.getBody();
        for(IASTNode n : body){
            if(n instanceof ASTTypeDeclarationStmtNode){
                if(((ASTTypeDeclarationStmtNode)n).getTypeSpec().isDerivedType()){
                    IASTListNode<ASTEntityDeclNode> entityDeclList = ((ASTTypeDeclarationStmtNode)n).getEntityDeclList();
                    for(ASTEntityDeclNode entity : entityDeclList){
                        if(entity.getObjectName().getObjectName().getText().equals(derivedTypeVariableName)){
                            match_name = true;
                        }
                    }
                }
            }
        }
        if(!match_name){
            fail(DERIVED_DATA_TYPE_INSTANCE_WARNING);
        }
    }

    @SuppressWarnings("static-access")
    @Override
    protected void doCreateChange(IProgressMonitor progressMonitor) throws CoreException, OperationCanceledException {
        List<Character> blank_characters = new LinkedList<Character>();
        IASTNode node = findEnclosingNode(astOfFileInEditor, selectedRegionInEditor);
        ScopingNode scope = astOfFileInEditor.getRoot().getEnclosingScope(node);
        String tab = ""; //$NON-NLS-1$
        String headerStmt = scope.getHeaderStmt().toString();
        String[] headerStmtWithoutComments = headerStmt.split("\n");
        headerStmt = headerStmtWithoutComments[headerStmtWithoutComments.length - 1];
        tab = getBlankCharacters(blank_characters, tab, headerStmt);
        tab+="\t\t"; //$NON-NLS-1$
        if(derived_data_type_ref != null){
            String new_statements = derived_data_type_ref.findToken().getText()+"\n";
            for(int i=0; i<statements_nodes.size(); i++){
                IBodyConstruct stmt = statements_nodes.get(i);
                new_statements += tab + ((ASTTypeDeclarationStmtNode)stmt).toString().trim();
                if(i<statements_nodes.size()-1){
                    new_statements += "\n"; //$NON-NLS-1$
                }
            }
            derived_data_type_ref.findToken().setText(new_statements);
        }
        // Search references of variables added to the derived type and replace the references by the derived type instance.
        findReferences(scope);
        if(statements_nodes.size() > 0){
            addChangeFromModifiedAST(fileInEditor, progressMonitor);
        }
        vpg.releaseAST(fileInEditor);
    }

    void findReferences(ScopingNode scope){
        List<Definition> used_definitions = new LinkedList<Definition>();
        List<Definition> all_definitions = scope.getAllDefinitions();
        LinkedList<String> node_names = new LinkedList<String>();
        // Gets the names of the derived type variables.
        for(IBodyConstruct t : statements_nodes){
            IASTListNode<ASTEntityDeclNode> statements = ((ASTTypeDeclarationStmtNode)t).getEntityDeclList();
            for(ASTEntityDeclNode d : statements){
                node_names.add(d.getObjectName().toString().trim());
            }
        }
        // Stores the definitions of variables actually used.
        for(Definition def : all_definitions){
            for(String n : node_names){
                if(def.getDeclaredName().toString().trim().equals(n)){
                    used_definitions.add(def);
                }
            }
        }
        // Stores references to each definition actually used.
        for(Definition d : used_definitions){
            Set<PhotranTokenRef> references_to_definition = d.findAllReferences(false);
            for(PhotranTokenRef t : references_to_definition){
                references.add(t);
            }
        }
        // Replaces references with the new type, with the access modifier %.
        for(PhotranTokenRef t : references){
            t.findToken().setText(derivedTypeVariableName+"%"+t.findToken().getText());
        }
        // Remove the declarations of variables that were included in the derived type.
        for(Definition def : used_definitions){
            try {
                removeVariableDeclFor(def);
            } catch (PreconditionFailure e) {
                e.printStackTrace();
            }
        }
    }

    private void removeVariableDeclFor(Definition def) throws PreconditionFailure {
        ASTTypeDeclarationStmtNode declarationNode = getTypeDeclarationStmtNode(def.getTokenRef().findToken().getParent());
        IASTListNode<ASTEntityDeclNode> entityDeclList = declarationNode.getEntityDeclList();
        if (entityDeclList.size() == 1) {
            declarationNode.findFirstToken().setWhiteBefore(""); //$NON-NLS-1$
            declarationNode.replaceWith(""); //$NON-NLS-1$
        }else {
            removeVariableDeclFromList(def, entityDeclList);
        }
    }

    private void removeVariableDeclFromList(Definition def, IASTListNode<ASTEntityDeclNode> entityDeclList) throws PreconditionFailure {
        for (ASTEntityDeclNode decl : entityDeclList) {
            ASTObjectNameNode objectName = decl.getObjectName();
            String declName = objectName.getObjectName().getText();
            if (declName.equals(def.getDeclaredName())) {
                if (!entityDeclList.remove(decl)) {
                    fail("The operation could not be completed.");
                }
                break;
            }
        }
        entityDeclList.findFirstToken().setWhiteBefore(" "); //$NON-NLS-1$
    }

    private ASTTypeDeclarationStmtNode getTypeDeclarationStmtNode(IASTNode node) {
        if (node == null){
            return null;
        }else if (node instanceof ASTTypeDeclarationStmtNode){
            return (ASTTypeDeclarationStmtNode)node;
        }else{
            return getTypeDeclarationStmtNode(node.getParent());
        }
    }

    // Get indentation.
    private String getBlankCharacters(List<Character> blank_characters, String tab, String headerStmt) {
        boolean start = false;
        for(int i=0; i<headerStmt.length(); i++){
            char c = headerStmt.charAt(i);
            if((c != '\t') && (c != ' ')){
                start = true;
            }
            if((c == '\t' || c == ' ') && !start){
                blank_characters.add(headerStmt.charAt(i));
            }
        }
        for(int i=0; i<blank_characters.size();i++){
            tab+=blank_characters.get(i);
        }
        return tab;
    }
}
