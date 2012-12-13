program main
       use sub
       use hello
        implicit none

       real :: a, b


       call printHello()

       a = 3.
       b = 7.
       write(*,'(2(A,f3.1," "))')"a: ",a,"b: ",b
       call swap(a,b)
       write(*,'(2(A,f3.1," "))')"a: ",a,"b: ",b
end program main
