# Write Python Code to Fetch Data and Store in SQLite

import sqlite3
import requests

# Function to fetch data from the API
def fetch_data_from_api():
    url = 'https://api.example.com/data'  # Replace with your API endpoint
    response = requests.get(url)
    if response.status_code == 200:
        return response.json()
    else:
        print(f"Failed to fetch data from API. Status code: {response.status_code}")
        return None

# Function to create SQLite database and table
def create_database():
    conn = sqlite3.connect('data.db')
    cursor = conn.cursor()

    # Create a table
    cursor.execute('''
        CREATE TABLE IF NOT EXISTS api_data (
            id INTEGER PRIMARY KEY,
            field1 TEXT,
            field2 INTEGER
        )
    ''')

    conn.commit()
    conn.close()

# Function to insert fetched data into SQLite database
def insert_data_into_database(data):
    conn = sqlite3.connect('data.db')
    cursor = conn.cursor()

    # Insert each record into the table
    for record in data:
        cursor.execute('''
            INSERT INTO api_data (field1, field2)
            VALUES (?, ?)
        ''', (record['field1'], record['field2']))

    conn.commit()
    conn.close()

# Main function to orchestrate fetching and storing data
def main():
    # Fetch data from API
    data = fetch_data_from_api()
    if data:
        # Create database and table
        create_database()
        # Insert data into database
        insert_data_into_database(data)

if __name__ == '__main__':
    main()
