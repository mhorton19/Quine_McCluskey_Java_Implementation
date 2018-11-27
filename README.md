# Quine_McCluskey_Java_Implementation
This is an implementation of the Quine_McCluskey algorthim which reduces sequential logic circuits (inputted as either a sum of products or product of sums) to its minimal form. This algorithm has the same purpose as karnaugh mapping, but it can be proven to be optimal and it can simplify a set of multiple functions sharing the same inputs.  This algorithm inherently scales exponentially with the number of variables, but the most time intensive parts could be done in parallel, so in the future I would like to utilize a GPU and program in a faster language such as C to speed this up and make it a viable way to simplify larger circuits. It is also very possible that having taken data structures since writing this, there are sections of the code that I could adapt to perform better. Additionally, if I do more work on this project, I will change it from reading user input to reading from a file for the sake of convinience and easy testing. This was initially a side project proposed by my computer engineering professor during my sophomore year, but I did add some features beyond the initial scope of the project and I would like to do more at a later time as well.
