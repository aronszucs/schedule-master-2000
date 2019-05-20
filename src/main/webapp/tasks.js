let tasksTableEl;
let tasksTableBodyEl;

function appendTasks(tasks) {
    removeAllChildren(tasksTableBodyEl);

    for (let i = 0; i < tasks.length; i++) {
        const task = tasks[i];
        appendTask(task);
    }
}

function onTasksLoad(tasks) {
    tasksTableEl = document.getElementById('tasks');
    tasksTableBodyEl = tasksTableEl.querySelector('tbody');

    appendTasks(tasks);
}

function onTaskClicked() {
    const taskId = this.dataset.taskId;
    hideBackToScheduleButton();
    showTask(taskId);
}

function showTask(taskId) {
    const params = new URLSearchParams();
    params.append('id', taskId);

    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onTaskResponse);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('GET', 'task?' + params.toString());
    xhr.send();
}


function onTaskLoad(schedules) {
    debugger;
    //taskId = schedules[0];
    //tasksTableBodyEl = tasksTableEl.querySelector('tbody');
    //const taskIdSpanEl = document.getElementById('task-id');
    const taskTitleSpanEl = document.getElementById('task-title');
    const taskContentSpanEl = document.getElementById('task-text');
    const taskSchedulesSpanEl = document.getElementById('task-schedules');
    const taskIdSpanEl = document.getElementById('hidden-id');

    removeAllChildren(taskSchedulesSpanEl);
    removeAllChildren(taskSchedulesSpanEl);

    taskIdSpanEl.value = schedules[0];
    taskIdSpanEl.dataset.taskId = schedules[0];
    taskTitleSpanEl.value = schedules[1];
    taskContentSpanEl.value = schedules[2];
    for(let i = 3; i < schedules.length-1; i+=2) {
        const scheduleLinkElement = document.createElement("a");
        scheduleLinkElement.textContent = schedules[i];
        scheduleLinkElement.dataset.scheduleId = schedules[i+1];
        scheduleLinkElement.addEventListener('click', onScheduleClicked);
        scheduleLinkElement.href = "javascript:void(0)";
        //taskSchedulesSpanEl.textContent += schedules[i] + " ";
        taskSchedulesSpanEl.appendChild(scheduleLinkElement);
    }
}

function onTasksResponse() {
    if (this.status === OK) {
        showContents(['tasks-content', 'back-to-profile-content', 'logout-content']);
        onTasksLoad(JSON.parse(this.responseText));
    } else {
        onOtherResponse(tasksContentDivEl, this);
    }
}

function onTaskAddResponse() {
    clearMessages();
    if (this.status === OK) {
        appendTask(JSON.parse(this.responseText));
    } else {
        onOtherResponse(tasksContentDivEl, this);
    }
}

function onTaskAddClicked() {
    const taskFormEl = document.forms['task-form'];

    const titleInputEl = taskFormEl.querySelector('input[name="title"]');
    const contentInputEl = taskFormEl.querySelector('input[name="content"]');

    const title = titleInputEl.value;
    const content = contentInputEl.value;

    const params = new URLSearchParams();
    params.append('title', title);
    params.append('content', content);

    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onTaskAddResponse);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('POST', 'protected/tasks');
    xhr.send(params);
}

