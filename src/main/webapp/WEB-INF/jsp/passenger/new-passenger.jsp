<%@include file="./../header.jsp" %>
<div class="main_body">
    <section>
        <div class="body_header">
            <h1> Passenger </h1>
        </div>
    </section>
    <div class="rail_form">
        <font color="red"><b><c:out value="${error}"/></b></font>
        <form action="/admin/ticket/add" method="post">
            <div>
                <label>Fisrt name:</label>
                <c:if test="${!(empty errorF)}">
                    <input type="text" name="first">
                    <font color="red"><b><c:out value="${errorF}"/></b></font>
                </c:if>
                <c:if test="${empty errorF}">
                    <input type="text" name="first">
                </c:if>
            </div>
            <div>
                <label>Last name:</label>
                <c:if test="${!(empty errorL)}">
                    <input type="text" name="last">
                    <font color="red"><b><c:out value="${errorL}"/></b></font>
                </c:if>
                <c:if test="${empty errorL}">
                    <input type="text" name="last">
                </c:if>
            </div>
            <div>
                <label>Birth:</label>
                <div class="form-group">
                    <div class="input-group date" id="datetimepicker1">
                        <input data-format="MM-dd-yyyy HH:mm:ss" type="text" name="birth"/>
                        <span class="input-group-addon">
                          <span class="glyphicon-calendar glyphicon"></span>
                      </span>
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
                            maxDate: today
                        });
                    });
                </script>
            </div>
            <div class="form_submit btn-default">
                <input type="submit" value="Add">
            </div>
        </form>
    </div>
</div>
<%@include file="./../bottom.jsp" %>