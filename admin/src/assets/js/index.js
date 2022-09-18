import $ from 'jquery';
import datepicker from 'bootstrap-datepicker';
import PerfectScrollbar from 'perfect-scrollbar';

$.fn.datepicker.dates['vi'] = {
  days: ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"],
  daysShort: ["CN", "T2", "T3", "T4", "T5", "T6", "T7"],
  daysMin: ["CN", "T2", "T3", "T4", "T5", "T6", "T7"],
  months: ["Tháng 1", "Tháng 2", "Tháng 3", "Tháng 4", "Tháng 5", "Tháng 6", "Tháng 7", "Tháng 8", "Tháng 9", "Tháng 10", "Tháng 11", "Tháng 12"],
  monthsShort: ["Th1", "Th2", "Th3", "Th4", "Th5", "Th6", "Th7", "Th8", "Th9", "Th10", "Th11", "Th12"],
  today: "Today",
  clear: "Clear",
  format: "mm/dd/yyyy",
  titleFormat: "MM yyyy",
  startDate: "01/01/1900",
  /* Leverages same syntax as 'format' */

};

$(document).mouseup(function () {
  // $('#checkAll').prop('indeterminate', true);
  // $('#checktag2').prop('indeterminate', true);
  // $('[data-toggle=tooltip]').tooltip();
  $('.input-daterange').datepicker({
    format: 'dd/mm/yyyy',
    autoclose: true,
    weekStart: 1,
    language: "vi",
    todayHighlight: true,
    startDate: "01/01/1900",
  })

  $('#from-date').on("change", function () {
    var startVal = $('#from-date').val();
    $('#to-date').data('datepicker').setStartDate(startVal);
  });
  $('#to-date').on("change", function () {
    var endVal = $('#to-date').val();
    $('#from-date').data('datepicker').setEndDate(endVal);
  });

  $('.datepicker-date').datepicker({
    format: 'dd/mm/yyyy',
    autoclose: true,
    weekStart: 1,
    language: "vi",
    todayHighlight: true,
    startDate: "01/01/1900",
  }).on('hide', function () {
    if (!this.firstHide) {
      if (!$(this).is(":focus")) {
        this.firstHide = true;
        this.focus();
      }
    } else {
      this.firstHide = false;
    }
  }).on('show', function () {
    if (this.firstHide) {
      $(this).datepicker('hide');
    }
  });
})
