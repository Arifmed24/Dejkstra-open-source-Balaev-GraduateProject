<%@include file="./../../header.jsp" %>
<div class="main_body">
    <section>
        <div class="body_header">
            <h1>New route</h1>
        </div>
    </section>
    <div class="rail_form">
        <form action="/admin/route/add" method="post">
            <div>
                <label>Route name</label>
                <input type="text" name="routeName"/>
            </div>
            <div>
                <label>Train</label>
                <select name="train">
                    <c:forEach items="${trains}" var="train">
                        <option value="${train.idTrain}">${train.idTrain}</option>
                    </c:forEach>
                </select>
            </div>
            <div>
                <label>Start station</label>
                <select name="startStation">
                    <c:forEach items="${stations}" var="station">
                        <option value="${station.idStation}">${station.stationName}</option>
                    </c:forEach>
                </select>
            </div>
            <div>
                <label>Finish station</label>
                <select name="finishStation">
                    <c:forEach items="${stations}" var="station">
                        <option value="${station.idStation}">${station.stationName}</option>
                    </c:forEach>
                </select>
                <font color="red"><b><c:out value="${error}"/></b></font>
            </div>
            <div class="double-button">
                <button name="createRoute" type="submit">Create route</button>
            </div>
        </form>
    </div>
</div>
<%@include file="./../../bottom.jsp" %>