import uuid
print(":".join([uuid.uuid1().hex[-12:][i : i + 2] for i in range(0, 11, 2)]))
