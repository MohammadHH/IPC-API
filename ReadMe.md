# IPC API

## Introduction

Generally, this API allows user to register themselves.When a user registers 
sucessfully, he is given an IPC container. Each IPC container has a queue of jobs (staged jobs). A job in IPC container represents a file that has been uploaded successfully.

A logedin user can print a file. To do that, he should ceate a press container, add the uploaded file to that container and then map the container to his IPC and then confirm the printing.

When the user add a file to the press container, the container puts the file in its held queue. After the user cinfirm the printing, the file goes into printing queue. When printing is done, the file is moved to a returned queue.

