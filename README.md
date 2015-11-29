# glang
The g language.

# Syntax
There are 3 types of symbols:
	- operator
	- operand
		- literal
		- variable
		- register
	- separator

Operators start with "@".
Variables start with "$".
Registers start with "#".
Separators are "::".

Compound expressions are NOT allowed. Ex: ```@print $v + $f``` is invalid.

There are 19 operators: ("value" refers to a literal, variable, or register.)

	- @init (variable)
		Defines a variable. You must @init a name before you use it.

	- @store (variable) :: (value)
		Stores a value in a variable.

	- @print (value)
		Prints a value to the console.

	- @printc (value)
		Prints a value to the console as a Unicode character.

	- @prints (string)
		Prints a string literal to the console. NOTE: @prints does not follow the normal syntax. 
		It prints all remaining characters on the line. Ex: @prints Hello world! will print "Hello world!" and @prints $v will print "$v".

	- @printl (string)
		Prints a string literal and a new line to the console. 
		NOTE: follows the same irregular syntax as @prints.

	- @newline
		Prints a new line to the console.

	- @+ (value) :: (value) :: (variable)
		Adds the two values and store the result in variable.

	- @- (value1) :: (value2) :: (variable)
		Subtracts value2 from value1 and stores the result in variable.

	- @* (value) :: (value) :: (variable)
		Multiplies the two values and store the result in variable.

	- @/ (value1) :: (value2) :: (variable)
		Divides value1 by value2 and stores the result in variable.

	- @jump (value)
		Jumps to value as a line number.

	- @jumpz (value1) :: (value2)
		Jumps to value2 if value1 is 0.

	- @jumpnz (value1) :: (value2)
		Jumps to value2 if value1 is not 0.

	- @jumpp (value1) :: (value2)
		Jumps to value2 if value1 is positive.

	- @jumpn (value1) :: (value2)
		Jumps to value2 if value1 is negative.

	- @equal (value1) :: (value2) :: (variable)
		If value1 is equal to value2, store 1 in variable, else, store 0 in variable.

	- @push (value)
		Push value onto the stack.

	- @pop (variable)
		Pop the value on the stack into variable.
