document.addEventListener('DOMContentLoaded', () => {
	const searchStartDate = document.getElementById('searchStartDate');
	const searchEndDate = document.getElementById('searchEndDate');
	const reportStartDate = document.getElementById('reportStartDate');
	const reportEndDate = document.getElementById('reportEndDate');
	const createDate = document.getElementById('date');

	flatpickr(searchStartDate, { maxDate: 'today' });
	flatpickr(searchEndDate, { maxDate: 'today' });
	flatpickr(reportStartDate, { maxDate: 'today' });
	flatpickr(reportEndDate, { maxDate: 'today' });
	flatpickr(createDate, { maxDate: 'today' });
});
