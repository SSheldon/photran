module sub
       implicit none

contains

       subroutine swap(a, b)
              real, intent(inout) :: a, b
              real :: c

              c = a
              a = b
              b = c
       end subroutine swap
end module sub
