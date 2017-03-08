<%@include file="./../../header.jsp" %>
<div class="main_body">
    <section>
        <div class="body_header">
            <h1> Ways </h1>
        </div>
        <div class="rr_table">
            <font color="red"><b><c:out value="${error}"/></b></font>
            <table>
                <tr>
                    <th> Variants</th>
                    <th> Passengers</th>
                </tr>
                <c:forEach items="${tickets}" var="ticket" varStatus="status">
                    <tr>
                        <td>
                            <c:set var="ticket" scope="session" value="${ticket}"/>
                            <b><i><c:out value="Variant: ${status.count}"/></i></b><br>
                            <c:out value="Train:${ticket.ticketTrain.idTrain}"/>
                            <br>
                            <c:out value="Departure:${ticket.departureStation.stationName}"/>
                            <c:out value=" // :${ticket.departureDate}"/>
                            <br>
                            <c:out value="Arrival:${ticket.arrivalStation.stationName}"/>
                            <c:out value=" // :${ticket.arrivalDate}"/>
                            <br>
                        </td>
                        <td>
                            <div class="rr_button">
                                <form action="/admin/way/passengers" method="post">
                                    <input type="hidden" name="index" value="${status.index}">
                                    <input class="btn-success " type="submit" value="View">
                                </form>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </div>
    </section>
</div>
<%@include file="./../../bottom.jsp" %>