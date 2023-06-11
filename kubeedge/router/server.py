import asyncio
import tornado
from tornado import web
from asyncio import runners
# print(tornado.version)


class MainHandler(web.RequestHandler):
    def get(self):
        self.write("Hello, world")

def make_app():
    return web.Application([
        (r"/bb", MainHandler),
    ])

async def main():
    app = make_app()
    app.listen(6666)
    await asyncio.Event().wait()

if __name__ == "__main__":

    runners.run(main())