import { useGoogleLogin } from '@react-oauth/google'
import toast from 'react-hot-toast'

interface LoginProps {
  onLogin: (token: string) => void
}

export default function Login({ onLogin }: LoginProps) {
  const login = useGoogleLogin({
    onSuccess: (tokenResponse) => {
      const token = tokenResponse.access_token
      localStorage.setItem('healthcare_token', token)
      onLogin(token)
      toast.success('Signed in successfully!')
    },
    onError: () => toast.error('Google sign-in failed'),
  })

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-blue-50 to-indigo-100">
      <div className="card p-10 max-w-md w-full mx-4 text-center">
        <div className="text-6xl mb-4">🏥</div>
        <h1 className="text-3xl font-bold text-gray-800 mb-2">HealthCare POC</h1>
        <p className="text-gray-500 mb-8">Manage doctors, patients and assignments</p>

        <button
          onClick={() => login()}
          className="w-full flex items-center justify-center gap-3 px-6 py-3 border border-gray-300 rounded-xl bg-white hover:bg-gray-50 shadow-sm transition-colors font-medium text-gray-700"
        >
          <svg width="20" height="20" viewBox="0 0 48 48">
            <path fill="#EA4335" d="M24 9.5c3.14 0 5.95 1.08 8.17 2.85l6.08-6.08C34.46 3.05 29.5 1 24 1 14.98 1 7.34 6.34 3.87 13.93l7.08 5.5C12.62 13.26 17.88 9.5 24 9.5z" />
            <path fill="#4285F4" d="M46.14 24.5c0-1.64-.15-3.22-.41-4.75H24v9h12.47c-.54 2.93-2.18 5.4-4.64 7.07l7.15 5.56C43.15 37.19 46.14 31.33 46.14 24.5z" />
            <path fill="#FBBC05" d="M10.95 28.57A14.37 14.37 0 0 1 9.5 24c0-1.6.28-3.14.77-4.57L3.19 13.93A23.03 23.03 0 0 0 1 24c0 3.68.87 7.16 2.41 10.24l7.54-5.67z" />
            <path fill="#34A853" d="M24 47c5.65 0 10.4-1.87 13.86-5.07l-7.15-5.56C28.99 37.6 26.61 38.5 24 38.5c-6.1 0-11.34-3.76-13.05-9.07l-7.54 5.67C7.34 41.66 14.98 47 24 47z" />
          </svg>
          Sign in with Google
        </button>

        <p className="mt-6 text-xs text-gray-400">
          Uses Google OAuth2 — same credentials as the backend
        </p>
      </div>
    </div>
  )
}
