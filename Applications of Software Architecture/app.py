# Setting up a Web Application


#!/usr/bin/env python3

from flask import Flask, request, render_template_string
import re

app = Flask(__name__)

# A simple HTML template using render_template_string
form_template = '''
    <h1>Weather Forecast App</h1>
    <p>Please enter date: DD/MM/YYYY</p>
    <form action="/echo_user_input" method="POST">
        <input name="user_input" placeholder="Enter date...">
        <input type="submit" value="Submit">
    </form>
    <p>{{ message }}</p>
'''

@app.route("/")
def main():
    return render_template_string(form_template, message="")

@app.route("/echo_user_input", methods=["POST"])
def echo_input():
    input_text = request.form.get("user_input", "")
    
    # Basic input validation for date in DD/MM/YYYY format
    if not re.match(r'^\d{2}/\d{2}/\d{4}$', input_text):
        return render_template_string(form_template, message="Invalid date format. Please use DD/MM/YYYY.")
    
    return render_template_string(form_template, message=f"You entered: {input_text}")

if __name__ == "__main__":
    app.run(debug=True)



