let gScheduleTable;
let gSelectedTaskId;
let gDragStartHour
let gDragStartDay
let scheduleId;


function showSchedule(scheduleTaskDto) {
    const scheduleContentEl = document.getElementById("schedule-content");
    removeAllChildren(scheduleContentEl);
    gScheduleId = scheduleTaskDto.schedule.id;
    gScheduleTable = new ScheduleTable("scheduleTable", scheduleTaskDto.schedule, scheduleTaskDto.taskDto)
    gScheduleTable.onFieldDragged = onTableFieldDragged;
    gScheduleTable.onFieldDropped = onTableFieldDropped;

    const tableContent = gScheduleTable.generateDom();
    scheduleContentEl.appendChild(tableContent);
    displayTaskPopup()
}

function showTasks(){

}

function deleteSchedule(){

}

function assignTask(){

}

function removeTask(){

}



//experimental code



function addTaskToDate() {
    const params = new URLSearchParams();
    params.append('scheduleId', gScheduleId);
    params.append("taskId", this.getAttribute("taskId"));
    params.append("row", gRow);
    params.append("col", gCol);

    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onScheduleClicked);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('POST', 'addTask?' + params.toString());
    xhr.send();
}
// old
function onTableBoxClicked(res) {
    const taskDto = res.task;
    const params = new URLSearchParams();
    params.append('scheduleId', gScheduleTable.schedule.id);

    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', displayTaskPopup);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('GET', 'addTask?' + params.toString());
    xhr.send();
}

function displayTaskPopup() {
    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onUserTasksReceived);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('GET', 'user_tasks');
    xhr.send();
}

function onUserTasksReceived() {
    const tasks = JSON.parse(this.responseText);
    var toDisplay = document.getElementById("pass");
    removeAllChildren(toDisplay);
    toDisplay.appendChild(document.createElement('br'));
    for (let i = 0; i < tasks.length; i++) {
        const task = tasks[i];
        const taskButton = document.createElement("button");
        taskButton.innerHTML = task.title;
        taskButton.className = "taskButton";
        taskButton.setAttribute("taskId", task.id);
        taskButton.addEventListener('click', onTaskSelectClicked);
        toDisplay.appendChild(taskButton);
        toDisplay.appendChild(document.createElement('br'));
    }
    //openWin();
}

function onTaskSelectClicked() {
    gSelectedTaskId = this.getAttribute("taskId");
}

function onTableFieldDragged(res) {
    gDragStartDay = res.clickedDay;
    gDragStartHour = res.clickedHour;
}

function onTableFieldDropped(res) {
    const params = new URLSearchParams();
    params.append('scheduleId', gScheduleTable.schedule.id);
    params.append('taskId', gSelectedTaskId);
    params.append('day', gDragStartDay);
    params.append('hourStart', gDragStartHour);
    params.append('hourEnd', res.clickedHour);

    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onTasksModified);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('POST', 'addTask?');
    xhr.send(params);
}

function onTasksModified() {
    const scheduleId = gScheduleTable.schedule.id;
    
    const params = new URLSearchParams();
    params.append('id', scheduleId);

    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onScheduleRefresh);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('GET', 'schedule?' + params.toString());
    xhr.send();
}

function onScheduleRefresh() {
    showSchedule(JSON.parse(this.responseText));
}