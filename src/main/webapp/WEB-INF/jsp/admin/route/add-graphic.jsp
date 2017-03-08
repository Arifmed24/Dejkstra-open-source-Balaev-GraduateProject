<%@include file="./../../header.jsp" %>
<div class="main_body">
    <section>
        <div class="body_header">
            <h1> New graphic of route ${route.routeName}</h1>
        </div>
        <form action="/admin/graphic/add" method="post">
            <div class="rr_table">
                <font color="red"><b><c:out value="${error}"/></b></font>
                <table>
                    <tr>
                        <th> Departure</th>
                        <th> Arrival</th>
                        <th> Date departure</th>
                        <th> Date arrival</th>
                    </tr>
                    <c:forEach var="routetimetable" items="${routeTimetables}" varStatus="count">
                        <tr>
                            <td><c:out value="${routetimetable.line.stationDeparture.stationName}"/></td>
                            <td><c:out value="${routetimetable.line.stationArrival.stationName}"/></td>
                            <td>
                                <div class="input-group date" id="datetimepicker${count.index*2+1}">
                                    <input data-format="MM-dd-yyyy HH:mm:ss" type="text"
                                           name="date_dep_${count.index+1}"/>
                                    <span class="input-group-addon">
                                                <span class="glyphicon-calendar glyphicon"/>
                                            </span>
                                </div>
                            </td>
                            <td>
                                <div class="input-group date" id="datetimepicker${count.index*2+2}">
                                    <input data-format="MM-dd-yyyy HH:mm:ss" type="text"
                                           name="date_arr_${count.index+1}"/>
                                    <span class="input-group-addon">
                                            <span class="glyphicon-calendar glyphicon"/>
                                        </span>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </div>
            <script type="text/javascript">
                var start = "<?=$date?>";
                var date = new Date();
                var today = new Date(date.getFullYear(), date.getMonth(), date.getDate());
                $(function () {
                    $('#datetimepicker1,#datetimepicker2,#datetimepicker3,#datetimepicker4,#datetimepicker5,#datetimepicker6,#datetimepicker7,#datetimepicker8,#datetimepicker9,#datetimepicker10').datetimepicker(
                        {
                            language: 'ru',
                            useSeconds: 'true',
                            format: 'DD/MM/YYYY HH:mm:ss',
                            defaultDate: start,
                            pickTime: true,
                            minDate: today
                        });
                });
            </script>
            <div class="rr_new_button">
                <div>
                    <button type="submit" style="width: 100%">Add</button>
                </div>
            </div>
        </form>
    </section>
</div>
<%@include file="./../../bottom.jsp" %>