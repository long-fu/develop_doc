# Echo client program
import my_socket

HOST = '192.168.8.48'    # The remote host
PORT = 7889              # The same port as used by the server
with my_socket.socket(my_socket.AF_INET, my_socket.SOCK_STREAM) as s:
    s.connect((HOST, PORT))
    s.sendall(b'0000000210')
    # s.close()
    data = s.recv(1024)
    print('Received', repr(data))
    s.close()
