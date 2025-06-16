
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
            await refreshProductTable();
            if (response.status === 500) {
                const errorData = await response.text();
                alert('Error al crear producto'+errorData);
            }
            return { mensaje: mensaje };
        }catch(error) {
            console.error('Error creating producto:', error);
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
                showSuccessModal('Categoría actualizada');
            }
            return productoNuevo;
        } catch (error) {
            console.error('Error updating producto:', error);
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
            console.log(response)
            if (response.status === 500) {
                hideLoadingModal();
                showErrorModal(mensaje);
            }else{
                if(entityType === 'producto') {
                hideLoadingModal();
                showSuccessModal(mensaje);
                refreshProductTable();
                }else if(entityType === 'categoria') {
                hideLoadingModal();
                llenarSelectProducto();
                refreshCategoryTable();
                showSuccessModal(mensaje);
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
            fetchCategoriaById(producto.idCategoria)
        );
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
                    <button type="button" class="nes-btn is-success">Confirmar</button>
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
    async function fetchCategoryById(id) {}
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
    // Limpiar la tabla (excepto el encabezado)
    while(tabla.rows.length > 1) {
        tabla.deleteRow(1);
    }
    // Volver a llenar la tabla
    await fillTableCategorias();
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
        const categoria = await fecthCategoriaById(categoriaId);
        inputNombre.value = categoria.nombre;
        inputDescripcion.value = categoria.descripcion;
        modal.dataset.categoriaId = categoriaId; // Guardar el ID de la categoría en el modal
        modal.showModal();
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
    document.getElementById('confirmProductSaveBtn').addEventListener('click', async function() {
        const nombreInput = document.getElementById('inputNombreNuevo');
        const precioInput = document.getElementById('inputPrecioNuevo');
        const categoriaIdInput = document.getElementById('categoriaSelectNueva');
        showLoadingModal();
        try{
            const resultado = await createProducto(nombreInput.value, precioInput.value, categoriaIdInput.value);
            nombreInput.value = '';
            precioInput.value = '';
            document.getElementById('categoriaSelectNueva').selectedIndex = 0;
            hideLoadingModal();
            showSuccessModal(resultado.mensaje);
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
    llenarSelectProducto();
    fillTableProducto();
    fillTableCategorias();
});