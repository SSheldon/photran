program commentIfConstructToIfStmt
    implicit none
    integer :: x, y, a
    if (x .LT. y .OR. y .GT. 5 .AND. 6 .GE. 6) then
        a = 1 !This is an if statement
    end if
    print *, "This is a test" !<<<<< 4, 5, 6, 11, pass
    
    !!! This test shows the refactoring successfully converting a valid IF construct to a valid IF statement, while
    !!! also preserving the included comment.
end program