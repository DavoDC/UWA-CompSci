
# Import flask
from flask import Flask

# Make app
app = Flask(__name__)

'''
# We bind URLs to functions
# - Default page
@app.route('/')
def func1():
   return 'Hello David :)'

# - Page 1
@app.route('/page1')
def func2():
   return 'Welcome to Page1!'

# - Dynamic url
@app.route('/add<int:num1>to<num2>')
def add(num1, num2):
    # Convert into int and add
    sum = num1 + int(num2)
    # Return as string
    return str(sum)
'''

from flask import url_for,redirect
'''
# URL redirection
@app.route('/')
def sayhello():
   return 'Hello'

@app.route('/<name>')
def sayhi(name):
   return f'Hi, {name}'

# http://127.0.0.1:5000/panel/David -> http://127.0.0.1:5000/David
@app.route('/panel/<salutation>')
def saysomething(salutation):
       if salutation=='guest':
               return redirect(url_for('sayhello'))
       else:
               return redirect(url_for('sayhi',name=salutation))
'''


# HTTP methods
'''
a. GET
The GET method sends data to the server unencrypted.
b. HEAD
HEAD is like GET, except that it has no response body.
c. POST
The server does not cache the HTML form data that this sends.
d. PUT
This uses the uploaded content to replace current representations of the target resource.
e. DELETE
This removes current representations of the target resource suggested by a URL.
Basically, the default route takes GET requests, but we can alter this by using the route() decorator with a method’s argument.
'''
from flask import request

@app.route('/success/<name>')
def success(name):
   return f'Hello, {name}'

@app.route('/login',methods=['POST','GET'])
def login():
   if request.method=='POST':
      user=request.form['firstname']
      return redirect(url_for('success',name=user))
   else:
      user=request.args.get('firstname')
      return redirect(url_for('success',name=user))


# If ran as main file, start site
if (__name__ == '__main__'):
    app.debug = True
    app.run()