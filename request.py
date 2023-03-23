import requests
import os
import sys

url = sys.argv[1]
folder = sys.argv[2]
filesType = sys.argv[3].upper()

for path in os.listdir(folder):
    files = {'file': open(os.path.join(folder, path), 'rb')}
    formData = {"type" : filesType}
    request = requests.post(url, files=files, data=formData)
    print(request.status_code)
