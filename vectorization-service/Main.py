from sentence_transformers import SentenceTransformer
from fastapi import FastAPI
from typing import List
from pydantic import BaseModel
import uvicorn

app = FastAPI()


class ResponseData(BaseModel):
    vectorizedData: List[List[float]]

class RequestData(BaseModel):
    dataToBeVectorized: List[str]

@app.post("/embeddings")
async def get_embeddings(request: RequestData):
    model = SentenceTransformer('sentence-transformers/all-mpnet-base-v2')
    embeddings = model.encode(request.dataToBeVectorized)
    return ResponseData(vectorizedData=embeddings.tolist())

if __name__ == "__main__":
    uvicorn.run(app, host="127.0.0.1", port=8000)