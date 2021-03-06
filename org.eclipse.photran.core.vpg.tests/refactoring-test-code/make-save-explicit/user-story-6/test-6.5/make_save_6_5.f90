! USER STORY 6, TEST 5
! Adds variable call_counter to SAVE statement for variable other_var due to
! call_counter being initialized in its declaration statement while sharing the
! same declaration statement with explicitly-saved variable other_var

PROGRAM MyProgram !<<<<< 1, 1, pass
  CALL MySub
  CALL MySub
END PROGRAM MyProgram

SUBROUTINE MySub
  SAVE other_var
  INTEGER :: other_var, call_counter = 0
  call_counter = call_counter + 1
  PRINT *, 'called:', call_counter
END SUBROUTINE MySub
