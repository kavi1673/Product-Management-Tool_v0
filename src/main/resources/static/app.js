async function jsonFetch(url, options = {}) {
	const res = await fetch(url, { headers: { 'Content-Type': 'application/json' }, ...options });
	const text = await res.text();
	let data; try { data = text ? JSON.parse(text) : null; } catch { data = text; }
	if (!res.ok) throw new Error((data && data.message) || res.statusText);
	return data;
}

function setOut(id, obj) {
	document.getElementById(id).textContent = JSON.stringify(obj, null, 2);
}

// Create Category
document.getElementById('btn-create-cat').onclick = async () => {
	const name = document.getElementById('cat-name').value.trim();
	const description = document.getElementById('cat-desc').value.trim();
	try {
		const data = await jsonFetch('/api/categories', { method: 'POST', body: JSON.stringify({ name, description }) });
		setOut('cat-out', data);
	} catch (e) { setOut('cat-out', { error: e.message }); }
};

// Add Attribute
document.getElementById('btn-add-attr').onclick = async () => {
	const categoryId = Number(document.getElementById('attr-cat-id').value);
	const name = document.getElementById('attr-name').value.trim();
	const dataType = document.getElementById('attr-type').value;
	const required = document.getElementById('attr-req').checked;
	const allowedValuesCsv = document.getElementById('attr-allowed').value.trim() || null;
	try {
		const data = await jsonFetch(`/api/categories/${categoryId}/attributes`, {
			method: 'POST',
			body: JSON.stringify({ name, dataType, required, allowedValuesCsv })
		});
		setOut('attr-out', data);
	} catch (e) { setOut('attr-out', { error: e.message }); }
};

// Load dynamic attrs
document.getElementById('btn-load-attrs').onclick = async () => {
	const categoryId = Number(document.getElementById('prod-cat-id').value);
	const container = document.getElementById('dyn-attrs');
	container.innerHTML = '';
	try {
		const attrs = await jsonFetch(`/api/categories/${categoryId}/attributes`);
		attrs.forEach(a => {
			const row = document.createElement('div');
			row.className = 'row';
			row.dataset.attrId = a.id;
			row.dataset.dataType = a.dataType;
			const label = document.createElement('label');
			label.textContent = `${a.name} (${a.dataType})${a.required ? ' *' : ''}`;
			let input;
			switch (a.dataType) {
				case 'STRING':
					input = document.createElement('input');
					if (a.allowedValuesCsv) {
						input.setAttribute('list', `dl-${a.id}`);
						const dl = document.createElement('datalist');
						dl.id = `dl-${a.id}`;
						a.allowedValuesCsv.split(',').map(s => s.trim()).filter(Boolean).forEach(v => {
							const opt = document.createElement('option'); opt.value = v; dl.appendChild(opt);
						});
						row.appendChild(dl);
					}
					break;
				case 'NUMBER':
					input = document.createElement('input'); input.type = 'number'; input.step = 'any'; break;
				case 'BOOLEAN':
					input = document.createElement('select');
					['', 'true', 'false'].forEach(v => { const o = document.createElement('option'); o.value = v; o.textContent = v; input.appendChild(o); });
					break;
				case 'DATE':
					input = document.createElement('input'); input.type = 'date'; break;
			}
			input.className = 'attr-input';
			row.appendChild(label);
			row.appendChild(input);
			container.appendChild(row);
		});
	} catch (e) {
		container.textContent = e.message;
	}
};

// Create Product
document.getElementById('btn-create-prod').onclick = async () => {
	const sku = document.getElementById('prod-sku').value.trim();
	const title = document.getElementById('prod-title').value.trim();
	const description = document.getElementById('prod-desc').value.trim();
	const categoryId = Number(document.getElementById('prod-cat-id').value);
	const priceText = document.getElementById('prod-price').value;
	const price = priceText ? Number(priceText) : null;
	const active = document.getElementById('prod-active').checked;
	const attrs = Array.from(document.querySelectorAll('#dyn-attrs .row')).map(row => {
		const id = Number(row.dataset.attrId);
		const type = row.dataset.dataType;
		const input = row.querySelector('.attr-input');
		switch (type) {
			case 'STRING': return { categoryAttributeId: id, valueString: input.value || null };
			case 'NUMBER': return { categoryAttributeId: id, valueNumber: input.value ? Number(input.value) : null };
			case 'BOOLEAN': return { categoryAttributeId: id, valueBoolean: input.value === '' ? null : input.value === 'true' };
			case 'DATE': return { categoryAttributeId: id, valueDate: input.value || null };
		}
	});
	try {
		const data = await jsonFetch('/api/products', {
			method: 'POST',
			body: JSON.stringify({ sku, title, description, categoryId, price, active, attributes: attrs })
		});
		setOut('prod-out', data);
	} catch (e) { setOut('prod-out', { error: e.message }); }
};

// Get Product
document.getElementById('btn-get-prod').onclick = async () => {
	const id = Number(document.getElementById('get-prod-id').value);
	try {
		const data = await jsonFetch(`/api/products/${id}`);
		setOut('get-prod-out', data);
	} catch (e) { setOut('get-prod-out', { error: e.message }); }
}; 