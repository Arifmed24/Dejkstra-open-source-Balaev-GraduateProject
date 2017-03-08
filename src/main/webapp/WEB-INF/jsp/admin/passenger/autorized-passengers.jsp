<%@include file="./../../header.jsp" %>
<div class="main_body">
    <section>
        <div class="body_header">
            <h1> Authorized passengers </h1>
        </div>
        <div class="rr_table">
            <table>
                <tr>
                    <th>Number</th>
                    <th>Passenger</th>
                </tr>
                <c:forEach var="passenger" items="${passengers}" varStatus="status">
                    <tr>
                        <td>
                            <c:out value="${status.count}"/>
                        </td>
                        <td>
                            <c:out value="${passenger.firstName} ${passenger.lastName}"/>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </div>
    </section>
    <div class="rail_form">
        <form action="/findway" method="GET">
            <div class="form_submit btn-default">
                <input type="submit" value="Go back">
            </div>
        </form>
    </div>
</div>
<%@include file="./../../bottom.jsp" %>