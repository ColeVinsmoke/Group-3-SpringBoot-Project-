const courses = [
  { code: "CSC101", title: "Intro to CS", credit: 3 },
  { code: "IFT201", title: "Data Structures", credit: 4 },
  { code: "CSC301", title: "Algorithms", credit: 4 },
  { code: "COS301", title: "Algorithms", credit: 4 },
  { code: "CSC201", title: "Data Structures", credit: 4 },
  { code: "CSC401", title: "Mathematics", credit: 4 }
];

let enrolled = JSON.parse(localStorage.getItem("enrolled")) || [];

const MAX = 24;
const MIN = 15;

function switchSection(id, el) {
  document.querySelectorAll(".section").forEach(s => s.classList.remove("active"));
  document.getElementById(id).classList.add("active");

  document.querySelectorAll(".nav-item").forEach(n => n.classList.remove("active"));
  el.classList.add("active");

//   document.getElementById("pageTitle").innerText = el.innerText;
}

function goCourses() {
  switchSection('courses', document.querySelectorAll('.nav-item')[1]);
}

function goMyCourses() {
  switchSection('mycourses', document.querySelectorAll('.nav-item')[2]);
}

function totalCredits() {
  return enrolled.reduce((sum, c) => sum + c.credit, 0);
}

function enroll(code) {
  const course = courses.find(c => c.code === code);

  if (enrolled.find(c => c.code === code)) return alert("Already enrolled");

  if (totalCredits() + course.credit > MAX)
    return alert("Max credit exceeded");

  enrolled.push(course);
  save();
}

function drop(code) {
  const course = enrolled.find(c => c.code === code);

//   if (totalCredits() - course.credit < MIN)
//     return alert("Cannot drop below minimum");

  enrolled = enrolled.filter(c => c.code !== code);
  save();
}

function save() {
  localStorage.setItem("enrolled", JSON.stringify(enrolled));
  renderAll();
}

function renderCourses() {
  const table = document.getElementById("courseTable");
  table.innerHTML = "";

  courses.forEach(c => {
    table.innerHTML += `
      <tr>
        <td>${c.code}</td>
        <td>${c.title}</td>
        <td>${c.credit}</td>
        <td><button onclick="enroll('${c.code}')">Enroll</button></td>
      </tr>
    `;
  });
}

function renderMyCourses() {
  const table = document.getElementById("myCourseTable");
  table.innerHTML = "";

  enrolled.forEach(c => {
    table.innerHTML += `
      <tr>
        <td>${c.code}</td>
        <td>${c.title}</td>
        <td>${c.credit}</td>
        <td><button class="delete" onclick="drop('${c.code}')">Drop</button></td>
      </tr>
    `;
  });
}

function updateDashboard() {
  const total = totalCredits();

  document.getElementById("totalCredits").innerText = total;
  document.getElementById("courseCount").innerText = enrolled.length;
  document.getElementById("bottomCredits").innerText = total;

  const status = document.getElementById("creditStatus");

  if (total < MIN) {
    status.innerText = "Below Minimum";
    status.style.background = "#fef3c7";
  } else if (total > MAX) {
    status.innerText = "Exceeded";
    status.style.background = "#fee2e2";
  } else {
    status.innerText = "OK";
    status.style.background = "#dcfce7";
  }
}
function renderAll() {
  renderCourses();
  renderMyCourses();
  updateDashboard();
}

renderAll();