
    async function fecthProductById(id){
        try {
            const response = await fetch(`http://localhost:5050/LAB/producto/${id}`);
            const data = await response.json();
            const producto = data;
            return producto;
        } catch (error) {
            console.error('Error fetching producto:', error);
            throw error;
        }
    }
    async function fecthCategoriaById(id){
        try {
            const response = await fetch(`http://localhost:5050/LAB/categoria/${id}`);
            const data = await response.json();
            const categoria = data;
            return categoria;
        } catch (error) {
            console.error('Error fetching categoria:', error);
            throw error;
        }
    }
    async function fetchInventarioById(idProducto) {
        try {
            const response = await fetch(`http://localhost:5050/LAB/inventario/${idProducto}`);
            const data = await response.json();
            const inventario = data;
            return inventario;
        } catch (error) {
            console.error('Error fetching inventario:', error);
            throw error;
        }
    } 
    async function fetchProducts() {
        try{
            const response = await fetch('http://localhost:5050/LAB/producto');
            const data = await response.json();
            const listaProductos = data
            return listaProductos;
        }catch (error) {
            console.error('Error fetching products:', error);
            throw error;
        }
    }
    async function createProducto(nombre, precio, idCategoria) {
        try{
            const nuevoProducto = {
                nombre: nombre,
                precio: Number(precio),
                idCategoria: Number(idCategoria)
            };
            const response = await fetch('http://localhost:5050/LAB/producto', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(nuevoProducto)
            });
            const mensaje = await response.text();
            llenarSelectInventario();
            await refreshProductTable();
            if (response.status === 500) {
                hideLoadingModal();
                showErrorModal('Error al crear producto');
            }else{
                hideLoadingModal();
                showSuccessModal(mensaje);
            }
            return { mensaje: mensaje };
        }catch(error) {
            console.error('Error creating producto:', error);
            throw error;
        }
    }
    async function createInventario(idProducto, cantidad) {
        try {
            const nuevoInventario = {
                idProducto: Number(idProducto),
                cantidad: Number(cantidad)
            };
            const response = await fetch('http://localhost:5050/LAB/inventario', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(nuevoInventario)
            });
            await refreshInventoryTable();
            llenarSelectInventario();
            llenarSelectProductoVenta();
            const mensaje = await response.text();
            console.log('Response status:', response.status);
            console.log('Mensaje de respuesta:', mensaje);
            if (response.status === 500) {
                hideLoadingModal();
                showErrorModal(mensaje);
            }else if(response.status === 409){
                hideLoadingModal();
                showErrorModal('El producto ya tiene un inventario asociado');
            }else if(response.status === 202){
                hideLoadingModal();
                showSuccessModal(mensaje);
            }
            return { mensaje: mensaje };
        } catch (error) {
            console.error('Error creating inventario:', error);
            throw error;
        }
    }
    async function createVenta(venta){
        try{
            const response = await fetch('http://localhost:5050/LAB/venta', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(venta)
            });
            const mensaje = await response.text();
            refreshInventoryTable();
            if (response.status === 500) {
                hideLoadingModal();
                showErrorModal('Error al crear venta');
            }else if(response.status === 409){
                hideLoadingModal();
                showErrorModal('El producto no tiene suficiente inventario');
            }else if(response.status === 200){
                hideLoadingModal();
                showSuccessModal(mensaje);
            }
        }catch (error) {
            console.error('Error creating venta:', error);
            showErrorModal('Error al crear venta');
        }
    }
    async function createFactura(monto,fecha){
        try {
            const nuevaFactura = {
                monto: Number(monto),
                fecha: fecha
            };
            const response = await fetch('http://localhost:5050/LAB/factura', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(nuevaFactura)
            });
            if (response.status === 500) {
                return null;
            }else{
                return response.text();
            }
        } catch (error) {
            console.error('Error creating factura:', error);
            throw error;
        }
    }
    async function updateProducto(id, nombre, precio, idCategoria) {
        try {
            showLoadingModal();
            const productoActualizado = {
                nombre: nombre,
                precio: Number(precio),
                idCategoria: Number(idCategoria)
            };
            const response = await fetch(`http://localhost:5050/LAB/producto/${id}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(productoActualizado)
            });
            await refreshProductTable();
            const productoNuevo = await response.json();
            if (response.status === 500) {
                hideLoadingModal();
                showErrorModal('Error al actualizar categoría');
            }else{
                hideLoadingModal();
                showSuccessModal('Producto actualizado');
            }
            return productoNuevo;
        } catch (error) {
            console.error('Error updating producto:', error);
            throw error;
        }
    }
    async function deleteEntity(entityType,id) {
        console.log(`Eliminando ${entityType} con ID:`, id);
        showLoadingModal();
        try{
            let endpoint = '';
            switch (entityType) {
                case 'producto':
                    endpoint = `http://localhost:5050/LAB/producto/${id}`;
                    break;
                case 'categoria':
                    endpoint = `http://localhost:5050/LAB/categoria/${id}`;
                    break;
                case 'inventario':
                    endpoint = `http://localhost:5050/LAB/inventario/${id}`;
                    break;
                default:
                    throw new Error('Tipo de entidad no soportado');
            }
            const response = await fetch(endpoint, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(id)
            });
            const mensaje = await response.text();
            if (response.status === 500) {
                hideLoadingModal();
                showErrorModal(mensaje);
            }else{
                switch (entityType) {
                    case 'producto':
                        hideLoadingModal();
                        showSuccessModal(mensaje);
                        llenarSelectInventario();
                        refreshProductTable();
                        break;
                    case 'categoria':
                        hideLoadingModal();
                        llenarSelectProducto();
                        refreshCategoryTable();
                        showSuccessModal(mensaje);
                        break;
                    case 'inventario':
                        hideLoadingModal();
                        showSuccessModal(mensaje);
                        llenarSelectProductoVenta();
                        refreshInventoryTable();
                        break;
                    default:
                        throw new Error('Tipo de entidad no soportado');
                }
            }
            }catch(error) {
            console.error(`Error eliminando ${entityType}:`, error);
            throw error;
            }
    }
    async function fillTableProducto(){
        try{
        const tablaProductos = document.getElementById('tableProdutos');
        const loadingRow = document.createElement('tr');
        loadingRow.innerHTML = `<td colspan="6" style="text-align: center;">Cargando productos...</td>`;
        tablaProductos.appendChild(loadingRow);
        const productos = await fetchProducts();
        const categoriasPromises = productos.map(producto => 
        fetchCategoriaById(producto.idCategoria));
        const categorias = await Promise.all(categoriasPromises);
        tablaProductos.removeChild(loadingRow);
        productos.forEach((producto, index) => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${producto.id}</td>
                <td>${producto.nombre}</td>
                <td>${producto.precio}</td>
                <td>${categorias[index].nombre}</td>
                <td>
                    <button type="button" class="nes-btn is-primary" onclick="openProductEdit(${producto.id})">Editar</button>
                </td>
                <td><button type="button" class="nes-btn is-error" onclick="showDeleteModal('producto', ${producto.id})">Eliminar</button></td>
            `;
            tablaProductos.appendChild(row);
        });
    }catch (error) {
        console.error('Error:', error);
    }
    };
    async function fillTableCategorias(){
        try {
            const tablaCategorias = document.getElementById('tableCategorias');
            const loadingRow = document.createElement('tr');
            loadingRow.innerHTML = `<td colspan="4" style="text-align: center;">Cargando categorías...</td>`;
            tablaCategorias.appendChild(loadingRow);
            const categorias = await fetchCategories();
            tablaCategorias.removeChild(loadingRow);
            categorias.forEach(categoria => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${categoria.id}</td>
                    <td>${categoria.nombre}</td>
                    <td>${categoria.descripcion}</td>
                    <td>
                        <button type="button" class="nes-btn is-primary" onclick="openCategoryEdit(${categoria.id})">Editar</button>
                    </td>
                    <td><button type="button" class="nes-btn is-error" onclick="showDeleteModal('categoria', ${categoria.id})">Eliminar</button></td>
                `;
                tablaCategorias.appendChild(row);
            });
        } catch (error) {
            console.error('Error filling categories table:', error);
        }
    }
    async function fillTableInventario(){
        try {
            const tablaInventario = document.getElementById('tableInventario').querySelector('tbody');
            const loadingRow = document.createElement('tr');
            loadingRow.innerHTML = `<td colspan="4" style="text-align: center;">Cargando inventario...</td>`;
            tablaInventario.appendChild(loadingRow);
            const inventario = await fetchInvenario();
            const productosPromises = inventario.map(item =>
                fecthProductById(item.idProducto));
            const productos = await Promise.all(productosPromises);
            tablaInventario.innerHTML = '';
            inventario.forEach((input,index) => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${productos[index].nombre}</td>
                    <td>${input.cantidad}</td>
                    <td>
                    <button type="button" class="nes-btn is-primary" onclick="openInventoryEdit(${input.id}, ${input.idProducto})">Editar</button>
                    </td>
                    <td>
                        <button type="button" class="nes-btn is-error" onclick="showDeleteModal('inventario', ${input.id})">Eliminar</button>
                    </td>
                    `;
                tablaInventario.appendChild(row);
            });
        } catch (error) {
            console.error('Error filling inventory table:', error);
        }
    }
    async function fetchCategories(){
        try {
            const response = await fetch('http://localhost:5050/LAB/categoria');
            const data = await response.json();
            return data;
        } catch (error) {
            console.error('Error fetching categories:', error);
            throw error;
        }
    }
    async function refreshProductTable() {
    const tablaProductos = document.getElementById('tableProdutos');
    // Limpiar la tabla (excepto el encabezado)
    while(tablaProductos.rows.length > 1) {
        tablaProductos.deleteRow(1);
    }
    await fillTableProducto();
    }
    async function refreshCategoryTable() {
    const tabla = document.getElementById('tableCategorias');
    while(tabla.rows.length > 1) {
        tabla.deleteRow(1);
    }
    await fillTableCategorias();
    }
    async function refreshInventoryTable() {
    const tabla = document.getElementById('tableInventario');
    while(tabla.rows.length > 1) {
        tabla.deleteRow(1);
    }
    await fillTableInventario();
    }
    async function fetchCategoriaById(id) {
        try {
            const response = await fetch(`http://localhost:5050/LAB/categoria/${id}`);
            const data = await response.json();
            const categoriaProducto = data;
            return categoriaProducto;
        } catch (error) {
            console.error('Error fetching category:', error);
            throw error;
        }
    }
    async function fetchCategorias() {
        try{
            const response = await fetch('http://localhost:5050/LAB/categoria');
            const data = await response.json();
            const listaCategorias = data
            return listaCategorias;
        }catch (error) {
            console.error('Error fetching categorias:', error);
            throw error;
        }
    }
    async function createCategoria(nombre, descripcion) {
        try{
            const nuevaCategoria = {
                nombre: nombre,
                descripcion: descripcion,
            };
            const response = await fetch('http://localhost:5050/LAB/categoria', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(nuevaCategoria)
            });
            const mensaje = await response.text();
            await refreshCategoryTable();
            if (!response.ok) {
                const errorData = await response.text();
                alert('Error al crear categoria'+errorData);
            }
            return { mensaje: mensaje };
        }catch(error) {
            console.error('Error creating categoria:', error);
            throw error;
        }
    }
    async function updateCategoria(id, nombre, descripcion) {
        try{
            showLoadingModal();
            const categoriaActualizada = {
                nombre: nombre,
                descripcion: descripcion
            };
            const response = await fetch(`http://localhost:5050/LAB/categoria/${id}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(categoriaActualizada)
            });
            await refreshCategoryTable();
            const categoriaNueva = await response.json();
            if (response.status === 500) {
                hideLoadingModal();
                showErrorModal('Error al actualizar categoría');
            }else{
                hideLoadingModal();
                showSuccessModal('Categoría actualizada');
            }
            return categoriaNueva;
        }catch (error) {
            console.error('Error updating categoria:', error);
            throw error;
        }
    }
    async function updateInventario(idInventario,cantidad) {
        try {
            showLoadingModal();
            const inventarioActualizado = {
                cantidad: Number(cantidad),
            };
            const response = await fetch(`http://localhost:5050/LAB/inventario/${idInventario}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(inventarioActualizado)
            });
            await refreshInventoryTable();
            const inventarioNuevo = await response.json();
            if (response.status === 500) {
                hideLoadingModal();
                showErrorModal('Error al actualizar inventario');
            }else{
                hideLoadingModal();
                showSuccessModal('Inventario actualizado');
            }
            return inventarioNuevo;
        } catch (error) {
            console.error('Error updating inventario:', error);
            throw error;
        }
    }
    async function fetchInvenario(){
        try {
            const response = await fetch('http://localhost:5050/LAB/inventario');
            const data = await response.json();
            return data;
        } catch (error) {
            console.error('Error fetching inventario:', error);
            throw error;
        }
    }
    async function openProductEdit(productId) {
        const modal = document.getElementById('editProductModal');
        const inputNombre = document.getElementById('inputNombre');
        const inputPrecio = document.getElementById('inputPrecio');
        const categoriaSelect = document.getElementById('categoriaSelect');
        const [product, categorias] = await Promise.all([
            fecthProductById(productId),
            fetchCategorias()
        ]);
        categorias.forEach(categoria => {
            const option = document.createElement('option');
            option.value = categoria.id;
            option.textContent = categoria.nombre;
            // Marcar como seleccionada la categoría del producto
            if (categoria.id === product.idCategoria) {
                option.selected = true;
            }
            categoriaSelect.appendChild(option);
        });
        inputNombre.value = product.nombre;
        inputPrecio.value = product.precio;
        modal.dataset.productId = productId; // Guardar el ID del producto en el modal
        modal.showModal();
    }
    async function openCategoryEdit(categoriaId) {
        const modal = document.getElementById('editCategoryModal');
        const inputNombre = document.getElementById('inputNombreCategoriaUpd');
        const inputDescripcion = document.getElementById('inputDescripCategoriaUpd');
        const categoria = await fetchCategoriaById(categoriaId);
        inputNombre.value = categoria.nombre;
        inputDescripcion.value = categoria.descripcion;
        modal.dataset.categoriaId = categoriaId;
        modal.showModal();
    }
    async function openInventoryEdit(inventarioId,idProducto) {
        const modal = document.getElementById('editInventoryModal');
        const productoInventario = document.getElementById('inputProductoInvent');
        const cantidadInput = document.getElementById('inputCantidadInventUpdate');
        const inventario = await fetchInventarioById(idProducto);
        const producto = await fecthProductById(inventario.idProducto);
        productoInventario.value = producto.nombre;
        cantidadInput.value = inventario.cantidad;
        modal.dataset.inventarioId = inventarioId; 
        modal.showModal();
    }
    async function completarVenta() {
        const table =  document.getElementById('tableVenta');
        showLoadingModal();
        try{
            const rows = Array.from(table.rows).filter(row => row.rowIndex !== 0); // Excluir la fila del encabezado
            if (rows.length === 0) {
                hideLoadingModal();
                showErrorModal('No hay productos en la venta');
                return;
            }else{
                const productosVenta = [];
                const ventas = []; 
                var totalVenta = 0;
                rows.forEach(row => {
                    const productoId = row.dataset.productId;
                    const nombreProducto = row.cells[0].textContent;
                    const cantidad = parseInt(row.cells[1].textContent, 10);
                    const total = parseFloat(row.cells[2].textContent);
                    totalVenta += total;
                    productosVenta.push({ productoId, nombreProducto, cantidad, total });
                    ventas.push({
                        cantidad: cantidad,
                        idProducto: parseInt(productoId),
                        idFactura: 5
                    });
                });
                var monto = totalVenta;
                var fecha =  new Date().toISOString().split('T')[0];
                idFactura = await createFactura(monto,fecha);
                console.log(ventas);
                console.log("idFactura: "+idFactura);
                if (idFactura === null) {
                    hideLoadingModal();
                    showErrorModal('Error al crear la factura');
                    return;
                }else{
                    ventas.forEach(async venta => {
                        console.log("VENTA: ", JSON.stringify(venta));
                        await createVenta(venta);
                    });
                    showSuccessModal('Venta completada con éxito');
                }
                hideLoadingModal();
                return productosVenta;
            }
        }catch(error){
            hideLoadingModal();
            console.error('Error al completar la venta:', error);
            showErrorModal('Error al completar la venta');
        }
    }
    async function llenarSelectProducto(){
        try{
            const selectCategoria = document.getElementById('categoriaSelectNueva');
            if (selectCategoria.options.length > 0) {
            selectCategoria.innerHTML = '';
        }
            const categorias = await fetchCategorias();
            const defaultOption = document.createElement('option');
            defaultOption.value = '';
            defaultOption.textContent = 'Seleccione una categoría';
            selectCategoria.appendChild(defaultOption);
            categorias.forEach(categoria => {
                const option = document.createElement('option');
                option.value = categoria.id;
                option.textContent = categoria.nombre;
                selectCategoria.appendChild(option);
            });
        }catch (error) {
            console.error('Error filling select:', error);
        }
    }
    async function llenarSelectProductoVenta(){
        try{
            const selectVenta = document.getElementById('productoVenta');
            if (selectVenta.options.length > 0) {
            selectVenta.innerHTML = '';
        }
            const producto = await fetchProducts();
            const defaultOption = document.createElement('option');
            defaultOption.value = '';
            defaultOption.textContent = 'Producto';
            selectVenta.appendChild(defaultOption);
            producto.forEach(producto => {
                const option = document.createElement('option');
                option.value = producto.id;
                option.textContent = producto.nombre;
                selectVenta.appendChild(option);
            });
        }catch (error) {
            console.error('Error filling select:', error);
        }
    }
    async function llenarSelectInventario(){
        try{
            const selectProducto = document.getElementById('productoSelectInventarioNuevo');
            if (selectProducto.options.length > 0) {
                selectProducto.innerHTML = '';
            }
            const productos = await fetchProducts();
            const defaultOption = document.createElement('option');
            defaultOption.value = '';
            defaultOption.textContent = 'Seleccione un producto';
            selectProducto.appendChild(defaultOption);
            productos.forEach(producto => {
                const option = document.createElement('option');
                option.value = producto.id;
                option.textContent = producto.nombre;
                selectProducto.appendChild(option);
            });
        }catch (error) {
            console.error('Error filling select:', error);
        }
    }
    function showSuccessModal(message) {
    const modal = document.getElementById('successModal');
    const successMessage = document.getElementById('successMessage');   
    successMessage.textContent = message;
    modal.showModal();
    setTimeout(() => {
        modal.close();
    }, 5000);
    }
    function showLoadingModal(){
        const modal = document.getElementById('loadingModal');
        modal.showModal();
    }
    function hideLoadingModal() {
        const modal = document.getElementById('loadingModal');
        modal.close();
    }
    function showDeleteModal(entityType,id) {
        const modal = document.getElementById('deleteModal');
        modal.dataset.entityType = entityType; // 'producto' o 'categoria'
        modal.dataset.entityId = id;
        modal.showModal();
    }
    function showErrorModal(message){
        const modal = document.getElementById('errorModal');
        const mensaje = document.getElementById('errorMessage');
        mensaje.textContent = message;
        modal.showModal();
    }
    function agregarFilaVenta(productoId,nombreProducto, cantidad,total) {
        const tablaVenta = document.getElementById('tableVenta');
        const row = document.createElement('tr');
        row.dataset.productId = productoId;
        row.innerHTML = `
            <td>${nombreProducto}</td>
            <td>${cantidad}</td>
            <td>${total}</td>
            <td><button type="button" class="nes-btn is-error">Eliminar</button></td>
        `;
        const btnEliminar = row.querySelector('button');
        btnEliminar.addEventListener('click', function() {
            row.remove();
        });
        tablaVenta.appendChild(row);
        const productoSelect = document.getElementById('productoVenta');
        const cantidadInput = document.getElementById('cantidadVenta');
        productoSelect.selectedIndex = 0;
        cantidadInput.value = ''; 
    }
    //**************************//
document.addEventListener('DOMContentLoaded', async function() {
    document.getElementById('confirmEditButtonProduct').addEventListener('click', async function() {
        const nombre = document.getElementById('inputNombre').value;
        const precio = document.getElementById('inputPrecio').value;
        const categoriaId = document.getElementById('categoriaSelect').value;
        const productId = document.getElementById('editProductModal').dataset.productId;
        try{
            await updateProducto(productId, nombre, precio, categoriaId);
        }catch(error) {
            console.error('Error updating product:', error);
        }
    });
    document.getElementById('confirmEditButtonCategory').addEventListener('click', async function() {
        const nombre = document.getElementById('inputNombreCategoriaUpd').value;
        const descripcion = document.getElementById('inputDescripCategoriaUpd').value;
        const categoriaId = document.getElementById('editCategoryModal').dataset.categoriaId;
        try{
            await updateCategoria(categoriaId, nombre, descripcion);
        }catch(error) {
            console.error('Error updating product:', error);
        }
    });
    document.getElementById('confirmEditButtonInventory').addEventListener('click', async function() {
        const cantidad = document.getElementById('inputCantidadInventUpdate').value;
        const inventarioId = document.getElementById('editInventoryModal').dataset.inventarioId;
        try{
            await updateInventario(inventarioId,cantidad);
        }catch(error) {
            console.error('Error updating inventario:', error);
        }

    });
    document.getElementById('confirmProductSaveBtn').addEventListener('click', async function() {
        const nombreInput = document.getElementById('inputNombreNuevo');
        const precioInput = document.getElementById('inputPrecioNuevo');
        const categoriaIdInput = document.getElementById('categoriaSelectNueva');
        showLoadingModal();
        try{
            await createProducto(nombreInput.value, precioInput.value, categoriaIdInput.value);
            nombreInput.value = '';
            precioInput.value = '';
            document.getElementById('categoriaSelectNueva').selectedIndex = 0;
        }catch(error) {
            console.error('Error creando product:', error);
        }
    });
    document.getElementById('confirmDeleteButton').addEventListener('click', async function(){
        const modal = document.getElementById('deleteModal');
        const entityType = modal.dataset.entityType;
        const entityId = modal.dataset.entityId;
        
        try {
            modal.close();
            await deleteEntity(entityType, entityId);
        } catch(error) {
            hideLoadingModal();
        }
    });
    document.getElementById('confirmCategoriaSaveBtn').addEventListener('click', async function() {
        const inputNombreCategoria = document.getElementById('inputNombreCategoria');
        const inputDescripcionCategoria = document.getElementById('inputDescripcion');
        showLoadingModal();
        try{
            const resultado = await createCategoria(inputNombreCategoria.value, inputDescripcionCategoria.value);
            inputNombreCategoria.value = '';
            inputDescripcionCategoria.value = '';
            hideLoadingModal();
            llenarSelectProducto();
            showSuccessModal(resultado.mensaje);
        }catch(error){
            console.error('Error creando categoria:', error);
        }
    });
    document.getElementById('confirmIventarioSaveBtn').addEventListener('click', async function() {
        const productoSelect = document.getElementById('productoSelectInventarioNuevo');
        const cantidadInput = document.getElementById('inputCantidadNuevo');
        showLoadingModal();
        try{
            await createInventario(productoSelect.value, cantidadInput.value);
            productoSelect.selectedIndex = 0;
            cantidadInput.value = '';
        }catch(error) {
            console.error('Error creando inventario:', error);
        }
    });
    document.getElementById('agregarRowVenta').addEventListener('click', async function() {
        const productoSelect = document.getElementById('productoVenta');
        const cantidadInput = document.getElementById('cantidadVenta');
        if (productoSelect.value === '' || cantidadInput.value === '') {
            showErrorModal('Por favor, seleccione un producto y una cantidad.');
            return;
        }else{
            producto = await fecthProductById(productoSelect.value);
            precio = producto.precio * (cantidadInput.value);
            agregarFilaVenta(productoSelect.value, producto.nombre, cantidadInput.value, precio);
        }
    });
    document.getElementById('confirmVentaBtn').addEventListener('click', async function() {
        try{
            completarVenta();
            const tableVenta = document.getElementById('tableVenta');
            while (tableVenta.rows.length > 1) {
                tableVenta.innerHTML=''; // Eliminar todas las filas excepto la primera (encabezado)
            }
        }catch(error) {
            console.error('Error al completar la venta:', error);
            showErrorModal('Error al completar la venta');
        }
    });
    llenarSelectProducto();
    llenarSelectInventario();
    llenarSelectProductoVenta();
    fillTableProducto();
    fillTableCategorias();
    fillTableInventario();

});