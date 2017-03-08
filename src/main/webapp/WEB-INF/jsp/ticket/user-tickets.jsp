<%@include file="./../header.jsp" %>
<div class="main_body">
    <section>
        <div class="body_header">
            <h1> User tickets </h1>
        </div>
        <div class="rr_table">
            <table>
                <tr>
                    <th>Field</th>
                    <th>Description</th>
                </tr>
                <c:forEach var="ticket" items="${user.tickets}" varStatus="status">
                <tr>
                    <td colspan="2"><b><c:out value="Ticket ${status.count}"/></b></td>
                <tr>
                </tr>
                <td>Passenger</td>
                <td>${ticket.ticketPassenger.firstName} ${ticket.ticketPassenger.lastName}</td>
                </tr>
                <tr>
                    <td>Station departure</td>
                    <td>${ticket.departureStation.stationName}</td>
                </tr>
                <tr>
                    <td>Date departure</td>
                    <td>${ticket.departureDate}</td>
                </tr>
                <tr>
                    <td>Station arrival</td>
                    <td>${ticket.arrivalStation.stationName}</td>
                </tr>
                <tr>
                    <td>Date arrival</td>
                    <td>${ticket.arrivalDate}</td>
                </tr>
                <tr>
                    <td>Train</td>
                    <td>${ticket.ticketTrain.idTrain}</td>
                </tr>
                <tr>
                    <td>Price</td>
                    <td>${ticket.price} RUB</td>
                </tr>
                <tr>
                    <td colspan="2"></td>
                <tr>
                    </c:forEach>
            </table>
        </div>
    </section>
</div>
<%@include file="./../bottom.jsp" %>