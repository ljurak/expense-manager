(function() {
	function openModal(e) {
		e.preventDefault();
		e.stopPropagation();
		
		const modal = document.querySelector('.modal');
		
		const modalContent = document.createElement('div');
		modalContent.classList.add('modal-cnt');
		
		const confirmationText = document.createElement('p');
		confirmationText.textContent = 'Are you sure you want to remove this item?';
		
		const removeButton = document.createElement('a');
		removeButton.classList.add('remove-btn');
		removeButton.textContent = 'Remove';
		removeButton.href = e.target.href;
		
		const closeButton = document.createElement('button');
		closeButton.type = 'button';
		closeButton.textContent = 'X';
		closeButton.classList.add('close-btn');
		closeButton.addEventListener('click', closeModal);
		
		modalContent.appendChild(confirmationText);
		modalContent.appendChild(removeButton);
		modalContent.appendChild(closeButton);
		modal.appendChild(modalContent);
		
		modal.classList.add('open');
	}

	function closeModal() {
		const modal = document.querySelector('.modal');
		const modalContent = modal.querySelector('.modal-cnt');
		
		if (modalContent !== null) {
			modal.classList.remove('open');		
			modalContent.remove();
		}
	}

	document.addEventListener('DOMContentLoaded', function() {
		const removeButtons = document.querySelectorAll('.expenses-list .remove-btn');
		for (const btn of removeButtons) {
			btn.addEventListener('click', openModal);
		}
		
		document.addEventListener('click', function(e) {
			if (e.target.closest('.modal-cnt') === null) {
				const modal = document.querySelector('.modal.open');
				if (modal) {
					modal.classList.remove('open');
					modal.querySelector('.modal-cnt').remove();
				}
			}
		});
	});
})();
