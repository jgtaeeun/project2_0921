import React, { useState } from 'react';
import './noticeWrite.css'; // 스타일 파일 추가
import { useNavigate } from 'react-router-dom';

function NoticeWrite() {
  const [title, setTitle] = useState('');
  const [content, setContent] = useState('');
  const [files, setFiles] = useState([]); // 백엔드에 보내는 파일 배열

  const navigate = useNavigate();

  const handleTitleChange = (e) => {
    setTitle(e.target.value);
  };

  const handleContentChange = (e) => {
    setContent(e.target.value);
  };

  const handleFileChange = async (e) => {
    const selectedFiles = Array.from(e.target.files); // 선택한 파일들을 배열로 변환
  
    if (selectedFiles.length === 0) return; // 선택된 파일이 없으면 종료
  
    try {
      const token = localStorage.getItem('authToken'); // 인증 토큰 가져오기
  
      // FormData를 생성하여 파일을 추가
      const formData = new FormData();
      selectedFiles.forEach(file => {
        formData.append('files', file); // 키값은 'files', 값은 파일
      });
  
      // 백엔드로 POST 요청 전송
      const response = await fetch('http://localhost:8080/member/community/filenameCheck', {
        method: 'POST',
        headers: {
          Authorization: `Bearer ${token}`, // 인증 토큰을 포함
        },
        body: formData,
      });
  
      if (response.ok) {
        const data = await response.json();
        console.log(data)

        if (data.length === 0) {
          setFiles(selectedFiles); // 중복 파일명이 없으면 상태 업데이트
        } else {
          const fileNames = data.join(', '); // 배열의 모든 파일명을 문자열로 연결
          alert(`동일한 파일명(${fileNames})이 존재합니다. 파일명을 다시 설정해주세요.`);
          e.target.value = ''; // 파일 입력 초기화
          setFiles([]); // 선택된 파일 상태 초기화
        }
      } else {
        alert('파일명 중복검사 실패했습니다.');
      }
    } catch (error) {
      console.error('오류 발생:', error);
      alert('서버와 통신하는 중 오류가 발생했습니다.');
    }
  };
  
  // 공지사항 등록 요청
  const handleSubmit = async (e) => {
    e.preventDefault();

    // 파일을 포함한 데이터 폼 구성 (FormData 사용)
    const formData = new FormData();
    formData.append('title', title); // 백엔드에서 기대하는 필드 이름 'title'
    formData.append('content', content); // 백엔드에서 기대하는 필드 이름 'content'
    
    // 모든 파일을 FormData에 추가
    files.forEach((file) => {
      formData.append('files', file); // 백엔드에서 기대하는 필드 이름 'files'
    });

    try {
      const token = localStorage.getItem('authToken'); // 인증 토큰 가져오기

      // 백엔드로 POST 요청 전송
      const response = await fetch('http://192.168.0.142:8080/member/community', {
        method: 'POST',
        headers: {
          Authorization: `Bearer ${token}`, // 인증 토큰을 포함
        },
        body: formData, // 폼 데이터를 전송
      });

      console.log(response);
      if (response.ok) {
        alert('공지사항이 등록되었습니다.');
        navigate('/notice'); // 등록 후 공지사항 목록으로 이동
      } else {
        alert('공지사항 등록에 실패했습니다.');
      }
    } catch (error) {
      console.error('오류 발생:', error);
      alert('서버와 통신하는 중 오류가 발생했습니다.');
    }
  };

  return (
    <div className="notice-write-page">
      <h2>공지사항 작성</h2>
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label htmlFor="title">제목(*)</label>
          <input
            type="text"
            id="title"
            value={title}
            onChange={handleTitleChange}
            required
          />
        </div>
        <div className="form-group">
          <label>내용</label>
          <textarea
            value={content}
            onChange={handleContentChange}
            rows="10"
            placeholder="내용을 입력하세요."
          />
        </div>
        <div className="form-group">
          
          <input 
            type="file" 
            id="files" 
            onChange={handleFileChange} 
            multiple // 여러 파일 선택 가능
          />
        </div>
 
        <div className="form-buttons">
          <button type="button" onClick={() => navigate('/notice')}>목록</button>
          <button type="submit">등록</button>
        </div>
      </form>
    </div>
  );
}

export default NoticeWrite;
