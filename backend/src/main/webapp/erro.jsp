<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!doctype html>
<html lang="pt-BR">
<head>
    <meta charset="utf-8">
    <title>Cannamed - Aviso</title>
    <link rel="stylesheet" href="crud.css">
</head>
<body>
<header>
    <a href="index.jsp">CANNAMED</a>
</header>
<main>
    <h1>Não foi possível concluir a operação</h1>
    <p id="msg"></p>
    <p><a href="javascript:history.back()">Voltar</a> · <a href="index.jsp">Início</a></p>
</main>
<script>
    document.getElementById('msg').textContent = new URLSearchParams(location.search).get('msg') || 'Verifique os dados.';
</script>
</body>
</html>