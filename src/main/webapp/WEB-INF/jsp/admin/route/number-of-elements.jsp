<%@include file="./../../header.jsp" %>
<div class="main_body">
    <section>
        <div class="body_header">
            <h1> Number stations in route</h1>
        </div>
    </section>
    <div class="rail_form">
        <form action="/admin/route/elements" method="post">
            <div>
                <c:if test="${empty error}">
                    <input type="text" name="elements">
                </c:if>
                <c:if test="${!empty error}">
                    <input type="text" name="elements">
                    <font color="red"><b><c:out value="${error}"/></b></font>
                </c:if>
            </div>
            <div>
                <button class="form_submit btn-default" type="submit" name="set">Set</button>
            </div>
        </form>
    </div>
</div>
<%@include file="./../../bottom.jsp" %>