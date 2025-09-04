from fastapi import FastAPI
app = FastAPI(title="chanme-fastapi", version="0.1.0")
@app.get("/ping")
def ping():
    return {"msg": "pong"}
