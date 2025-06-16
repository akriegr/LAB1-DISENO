
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
            await refreshTable();
            if (!response.ok) {
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
            await refreshTable();
            const productoNuevo = await response.json();
            return productoNuevo;
        } catch (error) {
            console.error('Error updating producto:', error);
            throw error;
        }
    }
    async function deleteProducto(id) {
        console.log('Eliminando producto con ID:', id);
        try{
                const response = await fetch(`http://localhost:5050/LAB/producto/${id}`, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(id)
            });
            const mensaje = await response.text();
            if (!response.ok) {
                showErrorModal(mensaje);
            }
            await refreshTable();
            return { mensaje: mensaje };
        }catch (error) {
            console.error('Error deleting producto:', error);
            throw error;
        }
    }
    async function updateTest() {
        alert('Actualizando producto...');
    }
    async function refreshTable() {
    const tablaProductos = document.getElementById('tableProdutos');
    // Limpiar la tabla (excepto el encabezado)
    while(tablaProductos.rows.length > 1) {
        tablaProductos.deleteRow(1);
    }
    // Volver a llenar la tabla
    await FillTable();
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
    async function FillTable(){
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
                <td><button type="button" class="nes-btn is-error" onclick="showDeleteModal(${producto.id})">Eliminar</button></td>
            `;
            tablaProductos.appendChild(row);
        });
    }catch (error) {
        console.error('Error:', error);
    }
    };
    async function llenarSelect(){
        try{
            const selectCategoria = document.getElementById('categoriaSelectNueva');
            const categorias = await fetchCategorias();
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
        modal.style.display = 'none';
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
    function showDeleteModal(id) {
        const modal = document.getElementById('deleteModal');
        modal.dataset.productId = id;
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
    document.getElementById('confirmEditButton').addEventListener('click', async function() {
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
    document.getElementById('confirmProductSaveBtn').addEventListener('click', async function() {
        showLoadingModal();
        const nombre = document.getElementById('inputNombreNuevo').value;
        const precio = document.getElementById('inputPrecioNuevo').value;
        const categoriaId = document.getElementById('categoriaSelectNueva').value;
        try{
            const resultado = await createProducto(nombre, precio, categoriaId);
            hideLoadingModal();
            showSuccessModal(resultado.mensaje);
        }catch(error) {
            console.error('Error creando product:', error);
        }
    });
    document.getElementById('confirmDeleteButton').addEventListener('click', async function(){
        const modal = document.getElementById('deleteModal');
        const productId = modal.dataset.productId; // Obtener el ID del modal
        try {
        modal.close();
        showLoadingModal();
        const resultado = await deleteProducto(productId);
        hideLoadingModal();
        showSuccessModal(resultado.mensaje);
    } catch(error) {
        console.error('Error eliminando producto:', error);
        hideLoadingModal();
        alert('Error al eliminar el producto');
    }
    });
    await llenarSelect();
    await FillTable();

});