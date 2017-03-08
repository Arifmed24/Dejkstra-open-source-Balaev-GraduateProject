<%@include file="./../header.jsp" %>
<div class="main_body">
    <section>
        <div class="body_header">
            <h1>Station: ${station}</h1>
            <h2 align="center"><i>Date: ${date}</i></h2>
        </div>
        <div class="rr_table">
            <h3>Departure</h3>
            <table>
                <tr>
                    <th> Train</th>
                    <th> Route</th>
                    <th> To</th>
                    <th> Time</th>
                </tr>
                <c:forEach var="tableDep" items="${tablesDep}">
                    <tr>
                        <td align="center"><c:out value="${tableDep.routeId.train.idTrain}"/></td>
                        <td align="center"><c:out value="${tableDep.routeId.routeName}"/></td>
                        <td><c:out value="${tableDep.routeId.finishStation.stationName}"/></td>
                        <td><c:out value="${tableDep.dateDeparture}"/></td>
                    </tr>
                </c:forEach>
            </table>
            <br>
            <br>
            <h3>Arrival</h3>
            <table>
                <tr>
                    <th> Train</th>
                    <th> Route</th>
                    <th> From</th>
                    <th> Time</th>
                </tr>
                <c:forEach var="tableArr" items="${tablesArr}">
                    <tr>
                        <td align="center"><c:out value="${tableArr.routeId.train.idTrain}"/></td>
                        <td align="center"><c:out value="${tableArr.routeId.routeName}"/></td>
                        <td align="center"><c:out value="${tableArr.routeId.startStation.stationName}"/></td>
                        <td align="center"><c:out value="${tableArr.dateArrival}"/></td>
                    </tr>
                </c:forEach>
            </table>
        </div>
    </section>
</div>
<%@include file="./../bottom.jsp" %>