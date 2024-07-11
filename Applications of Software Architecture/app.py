# Setting up a Web Application


#!/usr/bin/env python3

from flask import Flask, request

app = Flask(__name__)

@app.route("/")
def main():
    return '''
    <h1>Weather Forecast App</h1>
    <p>Please enter date: DD/MM/YYYY</p>
    <form action="/echo_user_input" method="POST">
        <input name="user_input" placeholder="Enter date...">
        <input type="submit" value="Submit">
    </form>
    '''

@app.route("/echo_user_input", methods=["POST"])
def echo_input():
    input_text = request.form.get("user_input", "")
    return f"You entered: {input_text}"

if __name__ == "__main__":
    app.run(debug=True)


