<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0">
    <jsp:directive.page contentType="application/xhtml+xml" />
    <html xmlns="http://www.w3.org/1999/xhtml">

    <head>
        <title>Error</title>
        <style>
            body {
                background-color: #f8f9fa;
                font-family: Arial, sans-serif;
                display: flex;
                justify-content: center;
                align-items: center;
                height: 100vh;
                margin: 0;
            }

            .error-box {
                text-align: center;
                padding: 40px;
                border: 1px solid #ccc;
                border-radius: 12px;
                background-color: white;
                box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
            }

            .error-text {
                font-size: 24px;
                color: #d9534f;
            }
        </style>
    </head>

    <body>
        <div class="error-box">
            <div class="error-text">Error — please try again.</div>
        </div>
    </body>

    </html>
</jsp:root>