<%@include file="./../../header.jsp" %>
<div class="main_body">
    <section>
        <div class="body_header">
            <h1> Fill new route </h1>
        </div>
    </section>
    <div class="rail_form">
        <form action="" method="post">
            <div class="rr_table fill_new_route">
                <font color="red"><b><c:out value="${error}"/></b></font>
                <table>
                    <tr>
                        <th> Departure</th>
                        <th> Arrival</th>
                    </tr>
                    <c:forEach var="element" items="${elements}" varStatus="count">
                        <tr>
                            <td>
                                <c:if test="${count.index == 0}">
                                    <input disabled type="text" value="${route.startStation.stationName}">
                                </c:if>
                                <c:if test="${count.index !=0}">
                                    <input id="result_${count.index}" type="text" disabled>
                                </c:if>
                            </td>
                            <td>
                                <c:if test="${count.index != number-1}">
                                    <select name="stationId_${count.index+1}" id="select_${count.index+1}"
                                            onchange="fillStation(id)">
                                        <c:forEach items="${stations}" var="s">
                                            <option value="${s.idStation}">${s.stationName}</option>
                                        </c:forEach>
                                    </select>
                                </c:if>
                                <c:if test="${count.index == number-1}">
                                    <input disabled type="text" value="${route.finishStation.stationName}">
                                </c:if>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <div class="input-group date" id="datetimepicker${count.index*2+1}">
                                    <input data-format="MM-dd-yyyy HH:mm:ss" type="text" style="width: initial"
                                           name="date_dep_${count.index+1}"/>
                                    <span class="input-group-addon">
                                                    <span class="glyphicon-calendar glyphicon"/>
                                                </span>
                                </div>
                            </td>
                            <td>
                                <div class="input-group date" id="datetimepicker${count.index*2+2}">
                                    <input data-format="MM-dd-yyyy HH:mm:ss" type="text" style="width: initial"
                                           name="date_arr_${count.index+1}"/>
                                    <span class="input-group-addon">
                                                <span class="glyphicon-calendar glyphicon"/>
                                            </span>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
                <div class="rr_new_button">
                    <div class="btn-primary create_route gbcmrf">
                        <button type="submit">Create</button>
                    </div>
                </div>
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
            <script>
                function fillStation(id) {
                    var elt = document.getElementById(id);

                    if (elt.selectedIndex == -1)
                        return null;

                    switch (id) {
                        case "select_1" : {
                            document.getElementById("result_1").value = elt.options[elt.selectedIndex].text;
                            break;
                        }
                        case "select_2" : {
                            document.getElementById("result_2").value = elt.options[elt.selectedIndex].text;
                            break;
                        }
                        case "select_3" : {
                            document.getElementById("result_3").value = elt.options[elt.selectedIndex].text;
                            break;
                        }
                    }
                }
            </script>
        </form>
    </div>
</div>
<%@include file="./../../bottom.jsp" %>