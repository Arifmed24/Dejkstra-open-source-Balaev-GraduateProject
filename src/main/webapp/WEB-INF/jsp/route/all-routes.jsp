<%@include file="./../header.jsp" %>
<div class="main_body">
    <section>
        <div class="body_header">
            <h1> Routes </h1>
        </div>
        <c:if test="${sessionScope.user.role eq 'ADMIN'}">
            <div class="rr_new_button">
                <div class="btn-primary">
                    <div class="btn-primary">
                        <a href="/newroute">New route</a>
                    </div>
                </div>
            </div>
        </c:if>
        <div class="rr_table">
            <table>
                <tr>
                    <th> Route</th>
                    <th> Name</th>
                    <th> Train</th>
                    <th> Start station</th>
                    <th> Finish station</th>
                    <c:if test="${sessionScope.user.role eq 'ADMIN'}">
                        <th> Add new graphic</th>
                    </c:if>
                </tr>
                <c:forEach var="route" items="${routes}">
                    <tr>
                        <td><c:out value="${route.idRoute}"/></td>
                        <td><c:out value="${route.routeName}"/></td>
                        <td><c:out value="${route.train.idTrain}"/></td>
                        <td><c:out value="${route.startStation.stationName}"/></td>
                        <td><c:out value="${route.finishStation.stationName}"/></td>
                        <c:if test="${sessionScope.user.role eq 'ADMIN'}">
                            <td>
                                <div class="rr_button">
                                    <form action="/admin/graphic/add" method="get">
                                        <input type="hidden" name="idRoute" value="${route.idRoute}">
                                        <input class="btn-success " type="submit" value="New graphic">
                                    </form>
                                </div>
                            </td>
                        </c:if>
                    </tr>
                </c:forEach>
            </table>
        </div>
    </section>
</div>
<%@include file="./../bottom.jsp" %>
