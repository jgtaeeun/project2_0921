import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';

function NoticeReWrite() {
  const { id } = useParams(); // Get notice ID from URL
  const [title, setTitle] = useState('');
  const [content, setContent] = useState('');
  const [files, setFiles] = useState([]); // Selected files to be sent to backend
  const [existingFiles, setExistingFiles] = useState([]); // To store existing file names
  const [loading, setLoading] = useState(false); // Loading state
  const navigate = useNavigate();
  
  // Fetch existing notice details on component mount
  useEffect(() => {
    const fetchNoticeDetail = async () => {
      try {
        const token = localStorage.getItem('authToken'); // Get auth token
        const response = await fetch(`http://192.168.0.142:8080/member/community/${id}`, {
          method: 'GET',
          headers: {
            Authorization: `Bearer ${token}`, // Include auth token
          },
        });

        if (response.ok) {
          const data = await response.json();
          setTitle(data.title); // Set title from fetched data
          setContent(data.content); // Set content from fetched data
          // Convert fileName string to an array
          setExistingFiles(data.fileName ? data.fileName.split(',') : []); // Store existing file names as an array
        } else {
          alert('해당 공지사항을 찾을 수 없습니다.');
          navigate('/notice'); // Navigate if not found
        }
      } catch (error) {
        console.error('오류 발생:', error);
        alert('서버와 통신하는 중 오류가 발생했습니다.');
        navigate('/notice'); // Navigate on error
      }
    };

    fetchNoticeDetail(); // Call the fetch function
  }, [id, navigate]);

  // Handle title input change
  const handleTitleChange = (e) => {
    setTitle(e.target.value);
  };

  // Handle content input change
  const handleContentChange = (e) => {
    setContent(e.target.value);
  };

  // Handle file input change
  const handleFileChange = async (e) => {
    const newFiles = Array.from(e.target.files); // Convert selected files to an array
  
    if (newFiles.length === 0) return; // Exit if no files are selected
  
    try {
        const token = localStorage.getItem('authToken'); // Get the auth token
  
        // Create FormData to append files for the request
        const formData = new FormData();
        newFiles.forEach(file => {
            formData.append('files', file); // Append each file to FormData
        });
  
        // Send POST request to check for filename duplicates
        const response = await fetch('http://192.168.0.142:8080/member/community/filenameCheck', {
            method: 'POST',
            headers: {
                Authorization: `Bearer ${token}`, // Include auth token
            },
            body: formData,
        });
  
        if (response.ok) {
            const data = await response.json();
            if (data.length === 0) {
                // If no duplicate file names, update the files state with file names
                setFiles(prevFiles => [...prevFiles, ...newFiles.map(file => file.name)]);
            } else {
                // Alert with duplicate file names and reset the input
                alert(`동일한 파일명(${data.join(', ')})이 존재합니다. 파일명을 다시 설정해주세요.`); // Display all duplicate names
                e.target.value = ''; // Clear file input
                setFiles([]); // Reset selected files state
            }
        } else {
            alert('파일명 중복검사 실패했습니다.');
        }
    } catch (error) {
        console.error('오류 발생:', error);
        alert('서버와 통신하는 중 오류가 발생했습니다.');
    }
};


  // Handle file removal
  const handleRemoveFile = (fileName) => {
    setExistingFiles(existingFiles.filter(file => file !== fileName));
  };

  // Submit the form
  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true); // Set loading to true

    // Create FormData for file upload
    const formData = new FormData();
    formData.append('title', title);
    formData.append('content', content);

    // Combine new files and existing files into one array
    const allFiles = [...files, ...existingFiles];

    allFiles.forEach(file => {
      // Check if the file is a string (existing file name)
      if (typeof file === 'string') {
        formData.append('existingFiles', file);
      } else {
        formData.append('files', file);
      }
    });

    try {
      const token = localStorage.getItem('authToken'); // Get auth token

      // Send PUT request to update the notice
      const response = await fetch(`http://192.168.0.142:8080/member/community/${id}`, {
        method: 'PUT',
        headers: {
          Authorization: `Bearer ${token}`, // Include auth token
        },
        body: formData,
      });

      if (response.ok) {
        alert('공지사항이 수정되었습니다.');
        navigate(`/notice/${id}`); // Navigate to the updated notice
      } else {
        alert(`공지사항 수정에 실패했습니다`);
      }
    } catch (error) {
      console.error('오류 발생:', error);
      alert('서버와 통신하는 중 오류가 발생했습니다.');
    } finally {
      setLoading(false); // Reset loading state
    }
  };

  return (
    <div className="notice-write-page">
      <h2>공지사항 수정</h2>
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
          <label>기존 파일들:</label>
          {existingFiles.map((fileName, index) => (
            <div key={index}>
              {fileName} 
              <button type="button" onClick={() => handleRemoveFile(fileName)}>삭제</button>
            </div>
          ))}
           <label>새로 첨부할 파일들:</label>
          {files.map((fileName, index) => (
            <div key={index}>{fileName}</div>
          ))}
          <input type="file" multiple onChange={handleFileChange} />
        </div>

        <div className="form-buttons">
          <button type="button" onClick={() => navigate('/notice')}>목록</button>
          <button type="submit" disabled={loading}>{loading ? '저장 중...' : '등록'}</button>
        </div>
      </form>
    </div>
  );
}

export default NoticeReWrite;
