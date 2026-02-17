from waitress import serve
from biomarker_backend.wsgi import application
import os

if __name__ == '__main__':
    port = os.environ.get('PORT', '8000')
    print(f"Serving on http://0.0.0.0:{port} ...")
    serve(application, host='0.0.0.0', port=port)
