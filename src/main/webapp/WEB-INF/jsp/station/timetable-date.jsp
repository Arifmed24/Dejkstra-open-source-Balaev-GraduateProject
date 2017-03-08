<%@include file="./../header.jsp" %>
<div class="main_body">
    <section>
        <div class="body_header">
            <h1> Choose station and date</h1>
        </div>
    </section>
    <div class="rail_form">
        <form action="/station/timetable" method="POST">
            <div>
                <label>
                    Station:
                </label>
                <select name="stationId">
                    <option selected value="${idStation}">${nameStation}</option>
                    <c:forEach items="${stations}" var="s">
                        <option value="${s.idStation}">${s.stationName}</option>
                    </c:forEach>
                </select>
            </div>
            <div>
                <label>
                    Date:
                </label>
                <div class="form-group">
                    <div class="input-group date" id="datetimepicker1">
                        <c:if test="${empty error}">
                            <input data-format="MM-dd-yyyy HH:mm:ss" type="text" name="date"/>
                            <span class="input-group-addon">
                                <span class="glyphicon-calendar glyphicon"></span>
                                </span>
                        </c:if>
                        <c:if test="${!empty error}">
                            <input data-format="MM-dd-yyyy HH:mm:ss" type="text" name="date"/>
                            <span class="input-group-addon">
                                <span class="glyphicon-calendar glyphicon"></span>
                                </span>
                            <font color="red"><b><c:out value="${error}"/></b></font>
                        </c:if>
                    </div>
                </div>
                <script type="text/javascript">
                    var start = "<?=$date?>";
                    var date = new Date();
                    var today = new Date(date.getFullYear(), date.getMonth(), date.getDate());
                    $(function () {
                        $('#datetimepicker1').datetimepicker({
                            language: 'ru',
                            useSeconds: 'true',
                            format: 'DD/MM/YYYY',
                            defaultDate: start,
                            pickTime: false,
                            minDate: today
                        });
                    });
                </script>

            </div>
            <div class="form_submit btn-default">
                <input type="submit" value="Get timetable "/>
            </div>
        </form>
    </div>
</div>
<%@include file="./../bottom.jsp" %>
