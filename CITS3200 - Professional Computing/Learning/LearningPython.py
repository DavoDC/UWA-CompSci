
# Imports
import math as doggy
import random

# start
print("Hello World")
print("\n")

# General
var = " apple"
print("val:", var)
var += " orange"
print("val:", var)
print("\n")

# Collections
fruits = ["apple", "mango", "orange"]  # list
numbers = (1, 2, 3)  # tuple
alphabets = {'a': 'apple', 'b': 'ball', 'c': 'cat'}  # dictionary
vowels = {'a', 'e', 'i', 'o', 'u'}  # set
mylist = [10, 8, 'a', "abc", ("pair1", "pair2"), ["list2"]]
print(mylist[:])
print("\n")

# Special strings
spec = "he said 'yeet'"
spec2 = 'yeet said "he"'
spec3 = """Big
ting"""
print(spec)
print(spec2)
print(spec3)
print("\n")

# String concatenation + MULTIPLICATION
str1 = 'Hello'
str2 = 'World!'
print('str1 + str2 = ', str1 + str2)
print('str1 * 3 =', str1 * 3)

# Rangelist
rangelist = list(range(10))
print(rangelist)
print("\n")

# Iteration + Conditional.
# Notice ':' + indentation + no brackets!
for num in range(5):
    if (num % 2 == 0):
        print("if")
    else:
        print("else")
print("\n")


# Iterate with index
genre = ['pop', 'rock', 'jazz']
for i in range(len(genre)):
    print("I like", genre[i])

# Loop with else
# program to display student's marks from record
student_name = 'Soyuj'
marks = {'James': 90, 'Jules': 55, 'Arthur': 77}
for student in marks:
    if student == student_name:
        print(marks[student])
        break
else:
    print('No entry with that name found.')


# While with else
counter = 0
while counter < 3:
    print("Inside loop")
    counter = counter + 1
else:
    print("Inside else")

# Function with arguments


def example(a_list, an_int=3, a_string="Thing"):
    a_list.append("Apple")
    an_int = 10
    return a_list, an_int, a_string


print(example([3, 4], 8, "Abc"))
print(example([7, 4]))
print("\n")

# Lambda func


def lambafunc(x): return x * 3


print(lambafunc(5))
print("\n")

# Func with multi args


def greet(*names):
    """This function greets all
    the person in the names tuple."""

    # names is a tuple with arguments
    for name in names:
        print("Hello", name)


greet("Monica", "Luke", "Steve", "John")

# Random lib
print(random.randint(0, 100))
print(random.randint(0, 100))
print(random.randint(25, 75))
print(random.randint(25, 75))
print("\n")

# Import with alias
print("\nThe value of pi is", doggy.pi)

# Classes
'''
class Dog(object):
    dna = 64
    def __init__(self):
        self.myvariable = 3

    def set(self, in_name):
        self.name = in_name
    def show():
        print(name)
print(Dog.dna)
dogInst = Dog()
dogInst.show
dogInst.set("Fido")
dogInst.show
'''


# end
print("\n")
