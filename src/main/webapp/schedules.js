let schedulesTableEl;
let schedulesTableBodyEl;
let gScheduleId;

function appendSchedules(schedules) {
    removeAllChildren(schedulesTableBodyEl);

    for (let i = 0; i < schedules.length; i++) {
        const schedule = schedules[i];
        appendSchedule(schedule);
    }
}

function onSchedulesLoad(schedules) {
    schedulesTableEl = document.getElementById('schedules');
    schedulesTableBodyEl = schedulesTableEl.querySelector('tbody');

    appendSchedules(schedules);
}

function onScheduleClicked() {
    const scheduleId = this.dataset.scheduleId;

    const params = new URLSearchParams();
    params.append('id', scheduleId);

    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onScheduleResponse);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('GET', 'schedule?' + params.toString());
    xhr.send();
}


function testFunction(result) {
    console.log(result);
}

function onScheduleLoad(scheduleTaskDto) {
    const scheduleContent = document.getElementById("schedule-content");
    removeAllChildren(scheduleContent);
    gScheduleId = scheduleTaskDto.schedule.id;
    const tableContent = createScheduleTable("The table that reigns supreme over all other tables in the known tableverse", scheduleTaskDto.schedule, scheduleTaskDto.taskDto, testFunction);
    scheduleContent.appendChild(tableContent);
}

function onSchedulesResponse() {
    if (this.status === OK) {
        showContents(['schedules-content', 'back-to-profile-content', 'logout-content']);
        onSchedulesLoad(JSON.parse(this.responseText));
    } else {
        onOtherResponse(schedulesContentDivEl, this);
    }
}

function onScheduleAddResponse() {
    clearMessages();
    if (this.status === OK) {
        appendSchedule(JSON.parse(this.responseText));
    } else {
        onOtherResponse(schedulesContentDivEl, this);
    }
}

function onScheduleAddClicked() {
    const scheduleFormEl = document.forms['schedule-form'];

    const nameInputEl = scheduleFormEl.querySelector('input[name="name"]');

    const name = nameInputEl.value;

    const params = new URLSearchParams();
    params.append('name', name);

    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onScheduleAddResponse);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('POST', 'protected/schedules');
    xhr.send(params);
}