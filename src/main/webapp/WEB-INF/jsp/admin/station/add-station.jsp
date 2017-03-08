<%@include file="./../../header.jsp" %>
<div class="main_body">
    <section>
        <div class="body_header">
            <h1> Create new station</h1>
        </div>
    </section>
    <div class="rail_form">
        <form action="/admin/station/add" method="POST">
            <div>
                <label>Name of station</label>
                <c:if test="${!(empty error)}">
                    <input type="text" name="name" value="${station.stationName}">
                    <font color="red"><b><c:out value="${error}"/></b></font>
                </c:if>
                <c:if test="${empty error}">
                    <input type="text" name="name" value="${station.stationName}">
                </c:if>
            </div>
            <div class="form_submit btn-default">
                <input type="submit" value="Create">
            </div>
        </form>
    </div>
</div>
<%@include file="./../../bottom.jsp" %>