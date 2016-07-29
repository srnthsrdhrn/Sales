import pickle

def get_coach_details(filename):
        with open(filename,'r') as file:
            print(file.readline())


file_name=input("Enter the name of the file")
get_coach_details(file_name)
