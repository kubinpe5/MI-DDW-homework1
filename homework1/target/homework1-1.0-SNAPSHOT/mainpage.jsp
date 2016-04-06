<%-- 
    Document   : mainpage
    Created on : 5.4.2016, 13:57:28
    Author     : Standard
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
    <head>
        <title>Keyword extraction</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- Latest compiled and minified CSS -->
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" 
              integrity="sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7" crossorigin="anonymous">
        <style>
            h1 {
                font-size: 4em;
                color: black;
                text-align: center;
            }

            table {
                margin-top: 1.5em;
                border: 2px solid black;
            }

            table>thead>tr>th,
            table>thead>tr>td {
                background-color: #cc181e;
                color: white;
                font-weight: bold;
                border: 2px solid black;

            }

            table>tbody>tr>td:nth-child(3) {
                text-align: center;
            }

            table>tbody>tr>th,
            table>tbody>tr>td{
                border: 0.8px solid #cc181e;
            }

            /* patička */
            #footer dt:before {
                content:"| ";
            }

            #footer dl:after {
                content:" |"; 
            }

            #footer dd, #footer dt {
                display: inline;
            }

            #footer dl {
                margin-top: 1em;
                padding: 2em;
                background-color: #cc181e;
                color: white;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <h1>Extraction of keywords from a web page</h1>

            <form method="POST" role="form">
                <div class="form-group">
                    <textarea name="text" placeholder="Zadejte text k hledání klíčových slov, můžete zkopírovat text odkudkoli, případně vložte URL adresu zdroje například: http://www.bbc.com/news/world-europe-35966412 ." class="form-control" style="height: 20em"><c:out value="${text}"/></textarea>
                </div>
                <button type="submit" value="submit" class="btn btn-primary">Submit</button>
            </form>
            <div>
                <table class="table">
                    <thead>
                        <tr>
                            <td>Klíčové slovo</td>
                            <td>Relevance</td>
                            <td>Počet výskytů</td>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${results}" var="result">
                            <tr>      
                                <td><c:out value="${result.key}"/></td>
                                <td>
                                    <div class="progress">
                                        <div class="progress-bar progress-bar-striped active" role="progressbar" aria-valuenow="70"
                                             aria-valuemin="0" aria-valuemax="100" style="width:<c:out value="${result.value.relevance*100}"/>%">
                                            <c:out value="${result.value.relevance*100}"/>%
                                        </div>
                                    </div>
                                <td><c:out value="${result.value.count}"/></td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>

            </div>
        </div>

        <div id="footer">
            <div class="container center">
                <dl class="text-muted">
                    <dt>Copyright</dt>
                    <dd>© 2016</dd>
                    <dt>Webmaster</dt>
                    <dd>Kubín Petr</dd>
                    <dt>Aktualizováno</dt>
                    <dd>6.4.2016</dd>
                </dl>
            </div>
        </div>
    </body>
</html>
