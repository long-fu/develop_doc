import asyncio
import tornado
import tornado.web as web
import json

class MainHandler(web.RequestHandler):
    def get(self):
        self.write("Hello, world")

class TestHandler(web.RequestHandler):
    def post(self):
        data = json.loads(self.request.body)
        print("接收的数据",data)
        # self.set_status(200)
        self.write(data)
        # self.finish()
    
class DeleteModelHandler(web.RequestHandler):
    def post(self):
        # 删除资源
        pass
    pass
class ModelSpecHandler(web.RequestHandler):
    def post(self):
        body = self.request.body
        text = body.decode('utf-8')
        post_json = json.loads(text)
        print(post_json)
        dic = {
            "message": "recive success"
        }
        self.set_status(200)
        self.write(dic)
        pass

class ModelStatusHandler(web.RequestHandler):
    def post(self):
        dic = {
            "retryCount": 1,
            "phase": "success",
            "message": "model download success"
        }
        self.set_status(200)
        self.write(dic)
        pass

def make_app():
    return web.Application([
        (r"/", MainHandler),
        (r"/api/smartbox/AlarmPost", TestHandler),
        (r"/modelSpec",ModelSpecHandler),
        (r"/modelStatus",ModelStatusHandler)
    ])

async def main():
    app = make_app()
    app.listen(8080)
    await asyncio.Event().wait()

if __name__ == "__main__":
    asyncio.run(main())