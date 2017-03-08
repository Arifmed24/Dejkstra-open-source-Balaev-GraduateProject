<%@include file="./../../header.jsp" %>
<div class="main_body">
    <section>
        <div class="body_header">
            <h1> Create new train</h1>
        </div>
    </section>
    <div class="rail_form">
        <form action="/admin/train/add" method="POST">
            <div>
                <label>Number of seats in train</label>
                <c:if test="${!(empty error)}">
                    <input type="text" name="seats" value="${train.seats}">
                    <font color="red"><b><c:out value="${error}"/></b></font>
                </c:if>
                <c:if test="${empty error}">
                    <input type="text" name="seats" value="${train.seats}">
                </c:if>
            </div>
            <div class="form_submit btn-default">
                <input type="submit" value="Create">
            </div>
        </form>
    </div>
</div>
<%@include file="./../../bottom.jsp" %>