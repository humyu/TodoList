
// 获取 DOM 元素
const todoForm = document.querySelector('.todo-form');
const todoInput = document.querySelector('.todo-input');
const todoList = document.querySelector('.todo-list');

const API_URL = 'http://localhost:8080/api/todos'

// 初始化核心数据数组
let todos = [];

// 核心渲染函数：根据 todos 数组来渲染页面
function render() {
    todoList.innerHTML = ''; // 每次渲染前清空列表
    todos.forEach(todo => {
        const li = document.createElement('li');
        li.classList.add('todo-item');
        li.setAttribute('data-id', todo.id); // 设置 data-id，方便后续操作

        if (todo.finished) {
            li.classList.add('completed');
        }

        li.innerHTML = `
            <input type="checkbox" class="toggle-complete" ${todo.finished ? 'checked' : ''}>
            <span class="todo-text">${todo.text}</span>
            <button class="edit-btn">🖋</button>
            <button class="delete-btn">×</button>
        `;
        todoList.appendChild(li);
    });
}

// 从后端加载数据
async function loadTodosFromServer() {
    try {
        const response = await fetch(API_URL);
        todos = await response.json();
        render();
    }catch (error){
        console.log('获取数据失败', error);
    }
}

// 事件监听：添加新事项
todoForm.addEventListener('submit', async function(event) {
    event.preventDefault(); // 阻止表单默认提交行为
    const newTodoText = todoInput.value.trim();
    console.log(todoInput.parentElement.classList.contains('completed'));

    if (newTodoText !== '') {
        const newTodo = {
            text: newTodoText,
            finished: false
        };

        try {
            const response = await fetch(API_URL, {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify(newTodo)
            })

            const savedTodo = await response.json();
            todos.push(savedTodo);

            todoInput.value = '';
            render();
        } catch (error) {
            console.error('添加失败：', error);
        }

    }
});

// 事件监听：是否完成事项
todoList.addEventListener('change', async function(event) {

    const target = event.target;
    const parentLi = target.closest('.todo-item');
    if (!parentLi) return;

    const todoId = Number(parentLi.getAttribute('data-id'));
    if (event.target.classList.contains('toggle-complete')) {
        const checkbox = event.target;
        const isChecked = checkbox.checked;
        // 找出修改状态的 id 对应的 todo 事项
        const todoToUpdate = todos.find(t => t.id === todoId);

        try {
            await fetch(`${API_URL}/${todoId}/status`, {
                method: 'PATCH',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({finished: isChecked})
            })

            todoToUpdate.finished = isChecked;
            render();
        } catch (error) {
            console.error('更新状态失败：', error);
            // 更新失败则还原状态
            target.checked = !isChecked;
        }
    }
})

// 事件监听：编辑和删除
todoList.addEventListener('click', async function(event) {
    const target = event.target;
    const parentLi = target.closest('.todo-item');
    if (!parentLi) return;

    const todoId = Number(parentLi.getAttribute('data-id'));

    // 编辑按钮
    if (target.classList.contains('edit-btn')) {
        const isEditing = parentLi.classList.contains('editing');
        // 当前不在编辑状态，就进入编辑模式
        if (!isEditing) {
            const span = parentLi.querySelector('span');
            const currentText = span.textContent;

            // 🌟 新增：把原文本存在 li 上，为了“点击空白处取消”时能恢复
            parentLi.dataset.originalText = currentText;

            // 2. 创建一个 input 元素
            const input = document.createElement('input');
            input.type = 'text';
            input.value = currentText;
            input.className = 'edit-input'; // 给个 class 方便写样式

            // 3. 用 input 替换 span
            span.replaceWith(input);

            // 4. 切换按钮文字为“保存”
            target.textContent = '📒';

            // 5. 给 li 添加 'editing' 状态，并让输入框自动聚焦
            parentLi.classList.add('editing');
            input.focus();
        }else {               // 当前是编辑状态就执行保存操作
            const input = parentLi.querySelector('.edit-input');
            const newText = input.value.trim();

            // 输入内容不能为空
            if (!newText) {
                alert('待办事项内容不能为空！');
                return;
            }

            try { // 准备要发送给后端的数据
                const updatedTodoData = {text: newText}; // 假设后端只需要更新 text 字段

                // 发送 PUT 请求更新数据  [^4^]
                const response = await fetch(`${API_URL}/${todoId}`, {
                    method: 'PATCH',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify(updatedTodoData),
                });

                if (response.ok) {
                    // 后端可能会返回完整的、更新后的 todo 对象
                    const savedTodo = await response.json();

                    // 🌟 最佳实践：使用 .map 更新本地数组  [^1^]
                    // 找到对应的 todo，用后端返回的最新数据替换它，其他保持不变
                    todos = todos.map(todo => {
                        if (todo.id === todoId) {
                            return savedTodo; // 使用后端返回的最新、最完整的数据
                        }
                        return todo;
                    });

                    // 重新渲染整个列表，li 会自动变回正常状态
                    render();

                } else {
                    alert('更新失败！');
                    target.disabled = false;
                    target.textContent = '保存';
                }
            } catch (error) {
                console.error("网络错误:", error);
                alert("网络连接错误！");
                target.disabled = false;
                target.textContent = '保存';
            }

        }
    }

    // 如果点击的是删除按钮
    if (target.classList.contains('delete-btn')) {
        try {
            const response = await fetch(`${API_URL}/${todoId}`, {
                method: 'DELETE',
            })

            // 从前端数组中移除并重新渲染
            todos = todos.filter(todo => todo.id !== todoId);
            render();

        } catch (error) {
            console.error('删除失败：', error);
        }
    }

});

// 监听整个文档的点击事件
document.addEventListener('click', function(event) {

    // 1. 尝试找到当前正在编辑的 <li>（如果没有，说明没在编辑，直接退出）
    const editingLi = document.querySelector('li.editing');
    if (!editingLi) return;

    // 2. 🌟 核心判断：判断鼠标点击的位置，是否在 editingLi 的【外部】
    // event.target 是你鼠标实际点到的元素
    // editingLi.contains(event.target) 如果为 false，说明点到了外面
    if (!editingLi.contains(event.target)) {

        // // 既然点到了外面，就执行【取消编辑，恢复原状】逻辑
        //
        // const input = editingLi.querySelector('.edit-input');
        // // 取回我们刚才存的原文本
        // const originalText = editingLi.dataset.originalText;
        //
        // // 重新创建一个 span，把原文本放进去
        // const span = document.createElement('span');
        // span.textContent = originalText;
        // span.className = 'todo-text';
        // input.replaceWith(span); // 替换掉 input
        //
        // // 按钮文字改回“修改”
        // const editButton = editingLi.querySelector('.edit-btn');
        // editButton.textContent = '🖋';
        //
        // // 移除编辑状态
        // editingLi.classList.remove('editing');

        render();
    }


});

loadTodosFromServer();

