from socket import *
from time import ctime

tasks = ['a', 'r']

HOST = '172.20.1.246';
PORT = 8080
BUFSIZE = 1024
ADDR = (HOST, PORT)


tcpSerSock = socket(AF_INET,SOCK_STREAM) 
print ("Socket successfully created")
#tcpSerSock = socket(AF_INET, SOCK_STREAM)
tcpSerSock.bind(ADDR)
print (ADDR)
tcpSerSock.listen(5)

while True:
    print ('Waiting for connection')
    tcpCliSock,addr = tcpSerSock.accept()
    print (addr)
    print ('...connected from: ', addr)
    try:
        while True:
            data = ''
            data = tcpCliSock.recv(BUFSIZE)
            if not data:
                break;
            print (data.decode(), ' is input data')
            if data.decode() == tasks[0]:
                print ('avoid in running')
                import avoid
                avoid.main()
                
            if data.decode() == tasks[1]:
                print ('recognition in running')
                import testing
                testing.main()
                
                

    except KeyboardInterrupt:
        avoid.close()
        testing.close()

tcpSerSock.close()