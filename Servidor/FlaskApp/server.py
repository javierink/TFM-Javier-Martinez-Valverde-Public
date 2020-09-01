from app import app
from flask import Flask
from gevent.pywsgi import WSGIServer


def lanzar_servidor_REST():

	http_server = WSGIServer(('0.0.0.0', 8000), app, keyfile='key.pem', certfile='cert.pem')
	http_server.serve_forever()


if __name__ == "__main__":
	lanzar_servidor_REST()
