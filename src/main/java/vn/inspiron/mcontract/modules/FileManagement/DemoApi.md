# 1. Settup certificate by file "viettel_ssl.cer" 

## 2. Demo api 

### 2.1 POST "/upload_file" 
    request: 
        file : File
    response:
        key of file
### 2.2 GET "/generate_pdf_url"
    request: 
        key : get from response of api 2.1
    response:
        link to view pdf file in expiration time
### 2.3 GET "/pdf/{encrypt}"
        get from api 2.2
### 2.4 DELETE "/delete_pdf"
    request: 
        key : get from response of api 2.1
    response
        key input