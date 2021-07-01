
# Imports
import math as doggy
import random



# Start
print("\nHello World")
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
        print("if", num)
    else:
        print("else", num)
print("\n")




# Iterate with index
genre = ['pop', 'rock', 'jazz']
for i in range(len(genre)):
    print("I like", genre[i])
print("\n")



# Loop with else
# program to display student's marks from record
marks = {'James': 90, 'Jules': 55, 'Arthur': 77}

def get_mark(student_name):
    for student in marks:
        if student == student_name:
            print(marks[student])
            return
    print('No entry with that name found.')
            
get_mark("John")
get_mark('James')
get_mark("Bunny")
print("\n")





# While with else
print("\nWhile test")
counter = 0
while counter < 3:
    print("Inside loop")
    counter = counter + 1
else:
    print("Inside else")
print("\n")




# Function with arguments
def example(a_list, an_int=3, an_int2 = 9, a_string="JT"):
    a_list.append("Apple")
    an_int = 10
    return a_list, an_int, an_int2, a_string

print(example([0, 0]))
print(example([1, 2], 4))
print(example([3, 4], 8, "Abc"))
print(example([5, 6], 8, "Abc", "XY"))
print("\n")




# Lambda func
print("\nLamba")
def lambafunc(x): return x * 3
print(lambafunc(5))
print(lambafunc(10))
print("\n")





# Func with multi args
print("\nMulti-arg func")
def greet(*names):
    """This function greets all
    the person in the names tuple."""

    # names is a tuple with arguments
    for name in names:
        print("Hello", name)
greet("Monica", "Luke", "Steve", "John")
print("\n")





# Random lib
print("\nRandom lib")
print(random.randint(0, 100))
print(random.randint(0, 100))
print(random.randint(25, 75))
print(random.randint(25, 75))
print("\n")



# Import with alias
print("\nThe value of pi is", doggy.pi)



# Classes
print("\nClasses\n")

# Class definition
class Person:
  
  # Constructor
  def __init__(self, name, age):
    # Initializing fields
    self.name = name
    self.age = age

  # Object method
  def myfunc(self):
    holder = "Hello, my name is " + self.name
    holder +=  " and I'm " + str(self.age)
    print(holder)

p1 = Person("John", 36)
p1.myfunc()

p2 = Person("Alex", 16)
p2.myfunc()

print("\n")

# end
print("\n")
