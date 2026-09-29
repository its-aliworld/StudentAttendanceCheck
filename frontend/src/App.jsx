import React, {useEffect, useState} from 'react'
import {request} from './api'

function Login({onLogin}) {
  const [email,setEmail]=useState('admin@example.com')
  const [password,setPassword]=useState('admin123')
  const [error,setError]=useState('')

  async function submit(e){
    e.preventDefault()
    setError('')
    try {
      const user=await request('/auth/login',{method:'POST',body:JSON.stringify({email,password})})
      onLogin(user)
    } catch(err){setError(err.message)}
  }

  return <div className="login-page">
    <form className="login-card" onSubmit={submit}>
      <div className="logo">✓</div>
      <h1>Attendance Portal</h1>
      <p>Student Attendance Management System</p>
      <input value={email} onChange={e=>setEmail(e.target.value)} placeholder="Email" type="email"/>
      <input value={password} onChange={e=>setPassword(e.target.value)} placeholder="Password" type="password"/>
      {error && <div className="error">{error}</div>}
      <button>Login</button>
      <small>Teacher: admin@example.com / admin123</small>
      <small>Student: student@example.com / student123</small>
    </form>
  </div>
}

function App(){
  const [user,setUser]=useState(()=>JSON.parse(localStorage.getItem('user')||'null'))

  function login(u){localStorage.setItem('user',JSON.stringify(u));setUser(u)}
  function logout(){localStorage.removeItem('user');setUser(null)}

  if(!user) return <Login onLogin={login}/>
  return <Dashboard user={user} logout={logout}/>
}

function Dashboard({user,logout}){
  const [students,setStudents]=useState([])
  const [subjects,setSubjects]=useState([])
  const [summary,setSummary]=useState(null)
  const [history,setHistory]=useState([])
  const [selectedStudent,setSelectedStudent]=useState('')
  const [selectedSubject,setSelectedSubject]=useState('')
  const [date,setDate]=useState(new Date().toISOString().slice(0,10))
  const [message,setMessage]=useState('')
  const [newStudent,setNewStudent]=useState({rollNumber:'',name:'',email:'',course:''})
  const [newSubject,setNewSubject]=useState({name:'',code:''})

  const isTeacher=user.role==='TEACHER'

  async function load(){
    const [s,sub]=await Promise.all([request('/students'),request('/subjects')])
    setStudents(s);setSubjects(sub)
    if(s.length && !selectedStudent)setSelectedStudent(String(s[0].id))
  }

  async function loadStudent(id){
    if(!id)return
    const [sum,hist]=await Promise.all([
      request('/attendance/summary/'+id),
      request('/attendance/student/'+id)
    ])
    setSummary(sum);setHistory(hist)
  }

  useEffect(()=>{load().catch(e=>setMessage(e.message))},[])
  useEffect(()=>{if(selectedStudent)loadStudent(selectedStudent).catch(e=>setMessage(e.message))},[selectedStudent])

  async function mark(studentId,status){
    if(!selectedSubject){setMessage('Select a subject first');return}
    try{
      await request('/attendance',{method:'POST',body:JSON.stringify({
        studentId:String(studentId),subjectId:String(selectedSubject),date,status
      })})
      setMessage('Attendance saved successfully')
      loadStudent(studentId)
    }catch(e){setMessage(e.message)}
  }

  async function addStudent(e){
    e.preventDefault()
    try{
      await request('/students',{method:'POST',body:JSON.stringify(newStudent)})
      setNewStudent({rollNumber:'',name:'',email:'',course:''})
      setMessage('Student added')
      load()
    }catch(e){setMessage(e.message)}
  }

  async function addSubject(e){
    e.preventDefault()
    try{
      await request('/subjects',{method:'POST',body:JSON.stringify(newSubject)})
      setNewSubject({name:'',code:''})
      setMessage('Subject added')
      load()
    }catch(e){setMessage(e.message)}
  }

  return <div className="app">
    <header>
      <div><strong>Attendance Portal</strong><span className="role">{user.role}</span></div>
      <div>{user.name} <button className="logout" onClick={logout}>Logout</button></div>
    </header>

    <main>
      <section className="hero">
        <div><h2>Attendance Dashboard</h2><p>Track and manage student attendance.</p></div>
        {message && <div className="message">{message}</div>}
      </section>

      <div className="cards">
        <div className="stat"><span>Total Classes</span><b>{summary?.total ?? 0}</b></div>
        <div className="stat"><span>Present</span><b>{summary?.present ?? 0}</b></div>
        <div className="stat"><span>Absent</span><b>{summary?.absent ?? 0}</b></div>
        <div className="stat"><span>Attendance</span><b>{summary?.percentage ?? 0}%</b></div>
      </div>

      <section className="panel">
        <h3>Mark Attendance</h3>
        <div className="controls">
          <select value={selectedStudent} onChange={e=>setSelectedStudent(e.target.value)}>
            <option value="">Select student</option>
            {students.map(s=><option key={s.id} value={s.id}>{s.rollNumber} - {s.name}</option>)}
          </select>
          <select value={selectedSubject} onChange={e=>setSelectedSubject(e.target.value)}>
            <option value="">Select subject</option>
            {subjects.map(s=><option key={s.id} value={s.id}>{s.code} - {s.name}</option>)}
          </select>
          <input type="date" value={date} onChange={e=>setDate(e.target.value)}/>
          {isTeacher && <>
            <button className="present" onClick={()=>mark(selectedStudent,'PRESENT')}>Present</button>
            <button className="absent" onClick={()=>mark(selectedStudent,'ABSENT')}>Absent</button>
          </>}
        </div>
      </section>

      <section className="panel">
        <h3>Students</h3>
        <table><thead><tr><th>Roll No.</th><th>Name</th><th>Email</th><th>Course</th></tr></thead>
        <tbody>{students.map(s=><tr key={s.id}><td>{s.rollNumber}</td><td>{s.name}</td><td>{s.email}</td><td>{s.course}</td></tr>)}</tbody></table>
      </section>

      <section className="panel">
        <h3>Attendance History</h3>
        <table><thead><tr><th>Date</th><th>Subject</th><th>Status</th></tr></thead>
        <tbody>{history.map(a=><tr key={a.id}><td>{a.date}</td><td>{a.subject.code} - {a.subject.name}</td><td><span className={a.status==='PRESENT'?'badge green':'badge red'}>{a.status}</span></td></tr>)}</tbody></table>
      </section>

      {isTeacher && <div className="admin-grid">
        <section className="panel">
          <h3>Add Student</h3>
          <form onSubmit={addStudent} className="form">
            <input placeholder="Roll number" value={newStudent.rollNumber} onChange={e=>setNewStudent({...newStudent,rollNumber:e.target.value})} required/>
            <input placeholder="Name" value={newStudent.name} onChange={e=>setNewStudent({...newStudent,name:e.target.value})} required/>
            <input placeholder="Email" type="email" value={newStudent.email} onChange={e=>setNewStudent({...newStudent,email:e.target.value})} required/>
            <input placeholder="Course" value={newStudent.course} onChange={e=>setNewStudent({...newStudent,course:e.target.value})}/>
            <button>Add Student</button>
          </form>
        </section>
        <section className="panel">
          <h3>Add Subject</h3>
          <form onSubmit={addSubject} className="form">
            <input placeholder="Subject name" value={newSubject.name} onChange={e=>setNewSubject({...newSubject,name:e.target.value})} required/>
            <input placeholder="Subject code" value={newSubject.code} onChange={e=>setNewSubject({...newSubject,code:e.target.value})} required/>
            <button>Add Subject</button>
          </form>
        </section>
      </div>}
    </main>
  </div>
}

export default App
